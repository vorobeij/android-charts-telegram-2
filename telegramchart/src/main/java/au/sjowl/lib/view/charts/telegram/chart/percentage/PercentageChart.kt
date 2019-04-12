package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class PercentageChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : AbstractChart(chartData, chartsData, chartLayoutParams) {

    override fun onDraw(canvas: Canvas) {
    }

    override fun fixPointsFrom() {
    }

    override fun updateOnAnimation() {
    }

    override fun calculatePoints() {
    }
}