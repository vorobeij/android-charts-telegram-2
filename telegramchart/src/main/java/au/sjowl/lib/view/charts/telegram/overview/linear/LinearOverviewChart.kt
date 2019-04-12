package au.sjowl.lib.view.charts.telegram.overview.linear

import android.content.Context
import au.sjowl.lib.view.charts.telegram.chart.linear.LinearChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.OverviewPaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class LinearOverviewChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : LinearChart(chartData, chartsData, chartLayoutParams) {

    override fun updateTheme(context: Context) {
        paints = OverviewPaints(context)
    }

    override fun calculateInnerBorders() {
        innerTimeIndexEnd = chartsData.times.size - 1
        innerTimeIndexStart = 0
    }

    override fun timeIndexStart() = 0
    override fun timeIndexEnd() = chartsData.times.size - 1
    override fun y(index: Int) = mh - kY * (chartData.values[index] - chartsData.chartsMin)
    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.chartValueInterval

    // todo draw lines instead of path
}