package au.sjowl.lib.view.charts.telegram.overview.stack

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView

class StackedBarOverviewChartView : BaseOverviewChartView {

    override fun calcExtremums() = chartsData.calcStackedChartExtremums()
    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return StackedBarOverviewChart(it, paints, chartLayoutParams, chartsData)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}