package au.sjowl.lib.view.charts.telegram.params

import android.content.Context

class ChartLayoutParams(context: Context) {
    val dimensions = ChartDimensions(context)

    var w = 0f

    var h = 0f

    val paddingBottom = 2

    val paddingTop = dimensions.chartPaddingTop

    val paddingTextBottom = dimensions.chartPaddingTextBottom

    val paddingHorizontal = dimensions.chartPaddingHorizontal

    val pointerCircleRadius = dimensions.chartPointerCircleRadius

    val yMarks = 5
}