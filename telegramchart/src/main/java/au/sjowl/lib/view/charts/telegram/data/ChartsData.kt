package au.sjowl.lib.view.charts.telegram.data

class ChartsData {

    val title: String = "Followers"

    val columns: MutableMap<String, ChartData> = mutableMapOf()

    val time: ChartAxisXData = ChartAxisXData()

    var type = ChartTypes.LINE

    var valueMin: Int = 0

    var valueMax: Int = 0

    var timeIndexStart = 0

    var timeIndexEnd = 0
        set(value) {
            field = if (value < timeIndexStart) timeIndexStart else value
        }

    var scaleInProgress = false

    val valueInterval get() = valueMax - valueMin

    val timeStart get() = time.values[timeIndexStart]

    val timeEnd get() = time.values[timeIndexEnd]

    val timeInterval get() = timeEnd - timeStart

    val timeIntervalIndexes get() = timeIndexEnd - timeIndexStart

    var pointerTimeIndex = 0

    val pointerTime get() = time.values[pointerTimeIndex]

    var pointerTimeX = 0f

    var isPercentage: Boolean = false

    var isStacked: Boolean = false

    var isYScaled: Boolean = false

    var canBeZoomed = true

    val isZoomed get() = !canBeZoomed

    fun initTimeWindow() {
        if (timeIndexStart != 0) return
        timeIndexStart = Math.max(time.values.lastIndex - 60, 0)
        timeIndexEnd = time.values.lastIndex
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
            val t = time.values
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
}