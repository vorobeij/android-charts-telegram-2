package au.sjowl.lib.view.charts.telegram.chart.container

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisStackedBars
import au.sjowl.lib.view.charts.telegram.chart.chartview.StackedBarsChartView

class StackedBarContainer : BaseChartContainer {

    override fun init() {
        chart = StackedBarsChartView(context)
        axisY = AxisStackedBars(context)
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