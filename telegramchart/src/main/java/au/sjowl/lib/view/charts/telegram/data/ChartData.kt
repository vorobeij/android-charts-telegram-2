package au.sjowl.lib.view.charts.telegram.data

import android.graphics.Color
import androidx.annotation.ColorInt

class ChartData(
    val id: String
) {

    var type: String = ChartTypes.LINE

    var name: String = ""

    val values: ArrayList<Int> = arrayListOf() // todo int array

    @ColorInt
    var color: Int = Color.RED

    var enabled: Boolean = true

    var windowMin: Int = 0

    var windowMax: Int = 0

    val height get() = windowMax - windowMin

    var chartMin = 0

    var chartMax = 0

    fun calculateBorders(start: Int = 0, end: Int = values.size - 1) {
        windowMin = Int.MAX_VALUE
        windowMax = Int.MIN_VALUE

        for (i in start..end) {
            val v = values[i]
            if (v < windowMin) windowMin = v
            if (v > windowMax) windowMax = v
        }
    }

    fun calculateExtremums() {
        chartMin = Int.MAX_VALUE
        chartMax = Int.MIN_VALUE

        for (i in 0 until values.size) {
            val v = values[i]
            if (v < chartMin) chartMin = v
            if (v > chartMax) chartMax = v
        }
    }
}

object ChartTypes {
    const val LINE = "line"
    const val AREA = "area"
    const val BAR = "bar"
}