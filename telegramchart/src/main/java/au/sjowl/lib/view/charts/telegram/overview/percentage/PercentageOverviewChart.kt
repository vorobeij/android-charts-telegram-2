package au.sjowl.lib.view.charts.telegram.overview.percentage

import android.content.Context
import android.graphics.Paint
import au.sjowl.lib.view.charts.telegram.chart.percentage.PercentageChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.overview.base.OverviewPaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class PercentageOverviewChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : PercentageChart(chartData, chartsData, chartLayoutParams) {

    override fun updateTheme(context: Context) {
        paints = OverviewPaints(context).apply {
            paintChartLine.style = Paint.Style.FILL
        }
    }

    override fun calculateInnerBorders() {
        innerTimeIndexEnd = chartsData.times.size - 1
        innerTimeIndexStart = 0
    }

    override fun timeIndexStart() = 0
    override fun timeIndexEnd() = chartsData.times.size - 1
    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.chartValueInterval

    // todo draw lines instead of path
}