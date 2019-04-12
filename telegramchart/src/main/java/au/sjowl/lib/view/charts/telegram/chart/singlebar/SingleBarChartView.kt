package au.sjowl.lib.view.charts.telegram.chart.singlebar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.chart.linear.LinearChart
import au.sjowl.lib.view.charts.telegram.chart.stack.StackBarChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class SingleBarChartView : BaseChartView {

    override fun calcExtremums() {
        return when {
            chartsData.isZoomed -> chartsData.calcLinearWindowExtremums()
            else -> chartsData.calcSingleBarWindowExtremums()
        }
    }

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return when {
            chartsData.isZoomed -> {
                LinearChart(it, value, chartLayoutParams)
            }
            else -> StackBarChart(it, value, chartLayoutParams)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}