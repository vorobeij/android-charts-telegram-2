package au.sjowl.lib.view.charts.telegram.chart.yscaled

import au.sjowl.lib.view.charts.telegram.chart.linear.LinearChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class YScaledChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : LinearChart(chartData, chartsData, chartLayoutParams) {
    override fun y(index: Int) = mh - kY * (chartData.values[index] - chartData.windowMin)
    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / (chartData.windowMax - chartData.windowMin)
}