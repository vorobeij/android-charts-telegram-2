package au.sjowl.lib.view.charts.telegram.data

import au.sjowl.lib.view.charts.telegram.chart.axis.ValueFormatter
import au.sjowl.lib.view.charts.telegram.other.SLog
import au.sjowl.lib.view.charts.telegram.params.ChartConfig

class ChartsData {

    var title: String = "Followers"

    val columns: MutableMap<String, ChartData> = mutableMapOf()

    val time: ChartAxisXData = ChartAxisXData()

    var type = ChartTypes.LINE

    var chartsMin: Int = 0
        private set

    var chartsMax: Int = 0
        private set

    var windowMin: Int = 0
        private set

    var windowMax: Int = 0
        private set

    var timeIndexStart = 0

    var timeIndexEnd = 0
        set(value) {
            field = if (value < timeIndexStart) timeIndexStart else value
        }

    var innerTimeIndexStart = 0

    var innerTimeIndexEnd = 0

    var scaleInProgress = false

    val windowValueInterval get() = windowMax - windowMin

    val chartValueInterval get() = chartsMax - chartsMin

    val timeStart get() = times[timeIndexStart]

    val timeEnd get() = times[timeIndexEnd]

    val timeInterval get() = timeEnd - timeStart

    val timeIntervalIndexes get() = timeIndexEnd - timeIndexStart

    var pointerTimeIndex = 0

    val pointerTime get() = times[pointerTimeIndex]

    var pointerTimeX = 0f

    var isPercentage: Boolean = false

    var isStacked: Boolean = false

    var isYScaled: Boolean = false

    var canBeZoomed = true

    val isZoomed get() = !canBeZoomed

    val isEmpty: Boolean get() = columns.isEmpty()

    val charts by lazy { columns.values.toList() }

    val times get() = time.values

    val size get() = times.size

    val sums by lazy { IntArray(size) }

    var barHalfWidth = 0f

    private var areExtremumsCalculated = false

    private val valueFormatter = ValueFormatter()

    fun initTimeWindow() {
        if (timeIndexStart != 0) return
        timeIndexStart = Math.max(times.lastIndex - 60, 0)
        timeIndexEnd = times.lastIndex
    }

    fun copyStatesFrom(chartsData: ChartsData) {
        // charts enabled
        for (key in columns.keys) {
            chartsData.columns[key]?.enabled?.let { columns[key]?.enabled = it }
        }

        if (isZoomed) { // zoom in to new data
            val day = 86_400_000
            val hours_2 = 3600_000 * 2
            timeIndexStart = 0
            val t = times
            val tStart = chartsData.pointerTime - hours_2
            while (t[timeIndexStart] < tStart && timeIndexStart < t.size) {
                timeIndexStart++
            }

            timeIndexEnd = t.size - 1
            val tEnd = chartsData.pointerTime + day - hours_2
            while (t[timeIndexEnd] > tEnd && timeIndexEnd > 0) {
                timeIndexEnd--
            }
        } else { // zoom out to new data
            val weeks_2 = 1_209_600_000
            timeIndexStart = 0
            val t = time.values
            val tStart = chartsData.timeStart - weeks_2
            while (t[timeIndexStart] < tStart && timeIndexStart < t.size) {
                timeIndexStart++
            }

            timeIndexEnd = t.size - 1
            val tEnd = chartsData.timeEnd + weeks_2
            while (t[timeIndexEnd] > tEnd && timeIndexEnd > 0) {
                timeIndexEnd--
            }
        }
    }

    fun calcChartsExtremums() {
        if (!areExtremumsCalculated) {
            charts.forEach { it.calculateExtremums() }
            areExtremumsCalculated = true
        } else {
            throw TwiceCallException("Charts extremums calculated twice")
        }
    }

    fun calcLinearWindowExtremums() {
//        SLog.d("calcLinearWindowExtremums before $windowMin, $windowMax")
        charts.forEach { it.calculateBorders(innerTimeIndexStart, innerTimeIndexEnd) }
        val enabled = charts.filter { it.enabled }
        windowMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
        windowMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100
        adjustAxisY()
//        SLog.d("calcLinearWindowExtremums after $windowMin, $windowMax")
//        SLog.d("calcLinearWindowExtremums $windowMin, $windowMax indexes:($timeIndexStart, $timeIndexEnd) inner:($innerTimeIndexStart, $innerTimeIndexEnd)")
    }

    fun calcLinearChartExtremums() {
        checkExtremumsCalculated()
        val enabled = charts.filter { it.enabled }
        chartsMin = enabled.minBy { it.chartMin }?.chartMin ?: 0
        chartsMax = enabled.maxBy { it.chartMax }?.chartMax ?: 100

//        SLog.d("calcLinearChartExtremums $chartsMin, $chartsMax")
    }

    fun calcPercentageChartExtremums() {
        calculateStack(0, times.size - 1)
        chartsMin = 0
        chartsMax = 100
    }

    fun calcPercentageWindowExtremums() {
        calculateStack(innerTimeIndexStart, innerTimeIndexEnd)
        windowMin = 0
        windowMax = 100
    }

    fun calcSingleBarChartExtremums() {
        chartsMin = 0
        chartsMax = charts[0].chartMax
    }

    fun calcSingleBarWindowExtremums() {
        charts.forEach { it.calculateBorders(innerTimeIndexStart, innerTimeIndexEnd) }
        windowMin = 0
        windowMax = charts[0].windowMax
        adjustAxisY()
    }

    /* do nothing, because chart don't take these values */
    fun calcYScaledChartExtremums() = Unit

    fun calcYScaledWindowExtremums() {
        charts.forEach { it.calculateBorders(innerTimeIndexStart, innerTimeIndexEnd) }
        SLog.d("TODO adjust borders for each of charts")
    }

    fun calcStackedChartExtremums() {
        chartsMin = 0
        chartsMax = stackedMax(0, times.size - 1)
    }

    fun calcStackedWindowExtremums() {
        windowMin = 0
        windowMax = stackedMax(innerTimeIndexStart, innerTimeIndexEnd)
        adjustAxisY()
    }

    private fun adjustAxisY() {
//        SLog.d("adjust before = $windowMin, $windowMax")
        val range = valueFormatter.adjustRange(windowMin, windowMax, ChartConfig.yIntervals)
        windowMin = range.min
        windowMax = range.max
        SLog.d("adjust after  = $windowMin, $windowMax")
    }

    private fun stackedMax(startIndex: Int, endIndex: Int): Int {
        if (isEmpty) return 1

        calculateStack(startIndex, endIndex)

        var max = 1
        for (i in startIndex..endIndex) {
            if (sums[i] > max) max = sums[i]
        }
        return max
    }

    private fun calculateStack(startIndex: Int, endIndex: Int) {
        for (i in startIndex..endIndex) {
            sums[i] = 0
        }
        for (j in 0 until charts.size) {
            if (charts[j].enabled) {
                for (i in startIndex..endIndex) {
                    sums[i] += charts[j].values[i]
                }
            }
        }
    }

    private inline fun checkExtremumsCalculated() {
        if (!areExtremumsCalculated) {
            throw MissedCallException("Charts extremums were not calculated before")
        }
    }
}

class TwiceCallException(message: String) : Exception(message)
class MissedCallException(message: String) : Exception(message)
