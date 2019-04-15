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
            else -> {
                chartsData.charts[0].enabled = true
                chartsData.calcSingleBarChartExtremums()
            }
        }
    }

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return when {
            chartsData.isZoomed -> {
                LinearOverviewChart(it, value, chartLayoutParams)
            }
            else -> {
                chartsData.charts[0].enabled = true
                StackedBarOverviewChart(it, value, chartLayoutParams)
            }
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}