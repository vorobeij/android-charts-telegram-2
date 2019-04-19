package au.sjowl.lib.view.charts.telegram.data

import au.sjowl.lib.view.charts.telegram.chart.base.axis.ValueFormatter
import au.sjowl.lib.view.charts.telegram.other.DateFormatter
import au.sjowl.lib.view.charts.telegram.params.ChartConfig

data class ChartsData(
    var title: String = "Followers"
) {

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

    val charts by lazy { columns.values.toList().sortedBy { it.indexAtJson } }

    val times get() = time.values

    val size get() = times.size

    val sums by lazy { IntArray(size) }

    var barHalfWidth = 0f

    var enabledChartsSum = 0

    private var areExtremumsCalculated = false

    private val valueFormatter = ValueFormatter()

    private var zoomOutStartIndex = 0

    private var zoomOutEndIndex = 0

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
            val range = DateFormatter.getDayBorders(chartsData.pointerTime)

            timeIndexStart = 0
            val t = times
            val tStart = range.start
            while (t[timeIndexStart] < tStart && timeIndexStart < t.size) {
                timeIndexStart++
            }

            timeIndexEnd = t.size - 1
            val tEnd = range.end
            while (t[timeIndexEnd] > tEnd && timeIndexEnd > 0) {
                timeIndexEnd--
            }

            zoomOutStartIndex = chartsData.timeIndexStart
            zoomOutEndIndex = chartsData.timeIndexEnd
        } else { // zoom out to new data
            timeIndexStart = chartsData.zoomOutStartIndex
            timeIndexEnd = chartsData.zoomOutEndIndex
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
        charts.forEach { it.calculateBorders(innerTimeIndexStart, innerTimeIndexEnd) }
        val enabled = charts.filter { it.enabled } // todo more efficient
        windowMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
        windowMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100
        adjustAxisY()
    }

    fun calcLinearChartExtremums() {
        checkExtremumsCalculated()
        val enabled = charts.filter { it.enabled }
        chartsMin = enabled.minBy { it.chartMin }?.chartMin ?: 0
        chartsMax = enabled.maxBy { it.chartMax }?.chartMax ?: 100
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
        charts.forEach {
            val r = valueFormatter.adjustRange(it.windowMin, it.windowMax, ChartConfig.yIntervals)
            it.windowMin = r.min
            it.windowMax = r.max
        }
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

    fun calcSums() {
        enabledChartsSum = 0
        charts.forEach { chart ->
            chart.calcSum(innerTimeIndexStart, innerTimeIndexEnd)
            if (chart.enabled) enabledChartsSum += chart.sum
        }
    }

    private fun adjustAxisY() {
        val range = valueFormatter.adjustRange(windowMin, windowMax, ChartConfig.yIntervals)
        windowMin = range.min
        windowMax = range.max
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
