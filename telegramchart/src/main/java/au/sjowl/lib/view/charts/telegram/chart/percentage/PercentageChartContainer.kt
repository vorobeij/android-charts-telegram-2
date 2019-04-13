package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.chart.linear.LineTintView

class PercentageChartContainer : BaseChartContainer {

    override fun init() {
        chart = PercentageChartView(context)
        tint = LineTintView(context)
        axisY = PercentageAxisView(context)
        pointerPopup = PercentagePointerPopup(context)

        super.init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}