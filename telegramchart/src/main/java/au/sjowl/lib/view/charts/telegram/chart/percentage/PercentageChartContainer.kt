package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.chart.linear.LineTintView
import au.sjowl.lib.view.charts.telegram.chart.pie.PieAxisView
import au.sjowl.lib.view.charts.telegram.chart.pie.PieChartView
import au.sjowl.lib.view.charts.telegram.chart.pie.PiePointerPopup

class PercentageChartContainer : BaseChartContainer {

    override fun onChartsDataChanged() {
        removeView(tint)
        removeView(axisY)
        removeView(chart)
        removeView(pointerPopup)

        if (chartsData.isZoomed) {
            tint = null
            axisY = PieAxisView(context)
            chart = PieChartView(context)
            pointerPopup = PiePointerPopup(context)
        } else {
            tint = LineTintView(context)
            axisY = PercentageAxisView(context)
            chart = PercentageChartView(context)
            pointerPopup = PercentagePointerPopup(context)
        }
        super.init()
        super.onChartsDataChanged()
    }

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