package au.sjowl.lib.view.charts.telegram.overview.scaledy

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView

class ScaleYOverviewChartView : BaseOverviewChartView {
    override fun calcExtremums() = chartsData.calcYScaledChartExtremums()

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return ScaleYOverviewChart(it, value, chartLayoutParams)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}