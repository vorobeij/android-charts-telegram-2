package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.chart.pie.PieChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class PercentageChartView : BaseChartView {

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return if (chartsData.isZoomed) {
            PieChart(it, value, chartLayoutParams)
        } else {
            PercentageChart(it, value, chartLayoutParams)
        }
    }

    override fun calcExtremums() = chartsData.calcPercentageWindowExtremums()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}