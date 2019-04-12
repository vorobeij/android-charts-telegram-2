package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class YScaledChartView : BaseChartView {
    override fun calcExtremums() = chartsData.calcYScaledWindowExtremums()

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return YScaledChart(it, value, chartLayoutParams)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}