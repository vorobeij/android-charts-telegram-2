package au.sjowl.lib.view.charts.telegram.overview.singlebar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView
import au.sjowl.lib.view.charts.telegram.overview.linear.LinearOverviewChart
import au.sjowl.lib.view.charts.telegram.overview.stack.StackedBarOverviewChart

class SingleBarOverviewChartView : BaseOverviewChartView {

    override fun calcExtremums() {
        return when {
            chartsData.isZoomed -> chartsData.calcLinearChartExtremums()
            else -> chartsData.calcSingleBarChartExtremums()
        }
    }

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return when {
            chartsData.isZoomed -> {
                LinearOverviewChart(it, paints, chartLayoutParams, chartsData)
            }
            else -> StackedBarOverviewChart(it, paints, chartLayoutParams, chartsData)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}