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
        timeIndexStart = Math.max(time.values.lastIndex - 60, 0)
        timeIndexEnd = time.values.lastIndex
    }

    fun copyStatesFrom(chartsData: ChartsData) {
        // charts enabled
        for (key in columns.keys) {
            chartsData.columns[key]?.enabled?.let { columns[key]?.enabled = it }
        }
        // todo window time index
    }
}