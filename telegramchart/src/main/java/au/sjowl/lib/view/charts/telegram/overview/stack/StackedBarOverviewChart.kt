package au.sjowl.lib.view.charts.telegram.overview.stack

import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.chart.stack.StackBarChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class StackedBarOverviewChart(
    chartData: ChartData,
    paints: BaseChartView.ChartViewPaints,
    chartLayoutParams: ChartLayoutParams,
    chartsData: ChartsData
) : StackBarChart(chartData, paints, chartLayoutParams, chartsData) {

    override fun calculateInnerBorders() {
        innerTimeIndexEnd = chartsData.times.size - 1
        innerTimeIndexStart = 0
    }

    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.chartValueInterval
    override fun timeIndexStart() = 0
    override fun timeIndexEnd() = chartsData.times.size - 1
}