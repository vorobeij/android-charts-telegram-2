package au.sjowl.lib.view.charts.telegram.chart.chartview.chart

import au.sjowl.lib.view.charts.telegram.chart.chartview.BaseChartView
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class LineChartYScaled(
    chartData: ChartData,
    paints: BaseChartView.ChartViewPaints,
    chartLayoutParams: ChartLayoutParams,
    chartsData: ChartsData
) : LineChart(chartData, paints, chartLayoutParams, chartsData) {
    override fun y(index: Int) = mh - kY * (chartData.values[index] - chartData.windowMin)
    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / (chartData.windowMax - chartData.windowMin)
}