package au.sjowl.lib.view.charts.telegram.overview.linear

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView

class LinearOverviewChartView : BaseOverviewChartView {

    override fun calcExtremums() = chartsData.calcLinearChartExtremums()

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return LinearOverviewChart(it, value, chartLayoutParams)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}
