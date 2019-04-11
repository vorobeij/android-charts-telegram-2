package au.sjowl.lib.view.charts.telegram.chart.linear

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class LinearChartView : BaseChartView {
    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return LinearChart(it, paints, chartLayoutParams, chartsData)
    }

    override fun calcExtremums() = chartsData.calcLinearWindowExtremums()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}