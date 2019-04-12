package au.sjowl.lib.view.charts.telegram.overview.stack

import android.content.Context
import au.sjowl.lib.view.charts.telegram.chart.stack.StackBarChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.OverviewPaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class StackedBarOverviewChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : StackBarChart(chartData, chartsData, chartLayoutParams) {

    override fun updateTheme(context: Context) {
        paints = OverviewPaints(context)
    }

    override fun calculateInnerBorders() {
        innerTimeIndexEnd = chartsData.times.size - 1
        innerTimeIndexStart = 0
    }

    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.chartValueInterval
    override fun timeIndexStart() = 0
    override fun timeIndexEnd() = chartsData.times.size - 1
}