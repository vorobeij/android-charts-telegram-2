package au.sjowl.lib.view.charts.telegram.chart.chartview

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.chartview.chart.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.chartview.chart.ChartStackedBar
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class StackedBarsChartView : BaseChartView {

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return ChartStackedBar(it, paints, chartLayoutParams, chartsData)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}