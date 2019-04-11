package au.sjowl.lib.view.charts.telegram.params

import android.content.Context

open class ChartLayoutParams(context: Context) {
    val dimensions = ChartDimensions(context)
    open var w = 0f
    open var h = 0f
    open val paddingBottom = 2
    open val paddingTop = dimensions.chartPaddingTop
    open val paddingHorizontal = dimensions.chartPaddingHorizontal
    val paddingTextBottom = dimensions.chartPaddingTextBottom
    val pointerCircleRadius = dimensions.chartPointerCircleRadius
    val yMarks = 5
}

class OverviewChartLayoutParams(context: Context) : ChartLayoutParams(context) {
    override val paddingBottom = dimensions.overviewPaddingVertical
    override val paddingTop = dimensions.overviewPaddingVertical
    override val paddingHorizontal = dimensions.overviewWindowBorder
}