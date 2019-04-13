package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.chart.base.ChartPointerPopup
import au.sjowl.lib.view.charts.telegram.chart.linear.LineTintView

class YScaledChartContainer : BaseChartContainer {

    override fun init() {
        chart = YScaledChartView(context)
        tint = LineTintView(context)
        axisY = ScaleYAxisView(context)
        pointerPopup = ChartPointerPopup(context)

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