package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class PercentageChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : AbstractChart(chartData, chartsData, chartLayoutParams) {

    protected val path = Path()

    private val columns = chartsData.charts

    private val chartIndex = chartsData.charts.indexOf(chartData)

    private var points = FloatArray(chartData.values.size * 2)

    private var pointsFrom = FloatArray(chartData.values.size * 2)

    private var drawingPoints = FloatArray(chartData.values.size * 2)

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paints.paintChartLine)
    }

    override fun calculatePoints() {
        var j = 0
        for (i in innerTimeIndexStart..innerTimeIndexEnd) {
            j = i * 2
            points[j] = x(i)
            points[j + 1] = y(i)
        }
    }

    override fun fixPointsFrom() {
        for (i in 2 * innerTimeIndexStart..(2 * innerTimeIndexEnd + 1)) {
            pointsFrom[i] = points[i]
        }
        enabled = chartData.enabled
    }

    override fun updateOnAnimation() {
        for (i in 2 * innerTimeIndexStart..2 * innerTimeIndexEnd step 2) {
            drawingPoints[i] = points[i]
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
        }
        updatePathFromPoints()
    }

    override fun updateTheme(context: Context) {
        paints = ChartPaints(context).apply {
            paintChartLine.style = Paint.Style.FILL
        }
    }

    override fun drawPointer(canvas: Canvas) = Unit

    protected open fun y(index: Int) = mh - kY * percentValue(index)

    private fun updatePathFromPoints() {
        with(path) {
            reset()
            if (drawingPoints.size > 1) {
                val start = 2 * innerTimeIndexStart
                val end = 2 * innerTimeIndexEnd
                moveTo(drawingPoints[start], chartLayoutParams.bottom)
                for (i in start..end step 2) {
                    lineTo(drawingPoints[i], drawingPoints[i + 1])
                }
                lineTo(drawingPoints[end], chartLayoutParams.bottom)
                close()
            }
        }
    }

    private inline fun percentValue(index: Int): Int {
        var y0 = 0
        for (k in 0..chartIndex) y0 += if (columns[k].enabled) columns[k].values[index] else 0
        return 100 * y0 / chartsData.sums[index]
    }
}