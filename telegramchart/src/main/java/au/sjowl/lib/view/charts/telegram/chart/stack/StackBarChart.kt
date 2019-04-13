package au.sjowl.lib.view.charts.telegram.chart.stack

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class StackBarChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : AbstractChart(chartData, chartsData, chartLayoutParams) {

    private val columns = chartsData.charts

    private val chartIndex = columns.indexOf(chartData)

    private var points = FloatArray(chartData.values.size * 4)

    private var pointsFrom = FloatArray(chartData.values.size * 4)

    private var drawingPoints = FloatArray(chartData.values.size * 4)

    private var strokeWidth = 1f

    override fun onDraw(canvas: Canvas) {
        setupPaint()
        canvas.drawLines(drawingPoints, innerTimeIndexStart * 4, drawingPointsSize() * 4, paints.paintChartLine)
    }

    override fun drawPointer(canvas: Canvas) {
        // draw tint
        paints.paintTint.strokeWidth = strokeWidth
        canvas.drawLines(drawingPoints, innerTimeIndexStart * 4, drawingPointsSize() * 4, paints.paintTint)
        // draw selected bar
        setupPaint()
        canvas.drawLines(drawingPoints, chartsData.pointerTimeIndex * 4, 4, paints.paintChartLine)
    }

    override fun calculatePoints() {
        var j = 0
        for (i in innerTimeIndexStart..innerTimeIndexEnd) {
            j = i * 4
            val y0 = y0(i)
            points[j] = x(i)
            points[j + 1] = y(y0)
            points[j + 2] = points[j]
            points[j + 3] = y(y0 + if (chartData.enabled) chartData.values[i] else 0)
        }
    }

    override fun fixPointsFrom() {
        drawingPoints.copyInto(pointsFrom)
    }

    override fun updateOnAnimation() {
        for (i in 4 * innerTimeIndexStart..4 * innerTimeIndexEnd step 4) {
            drawingPoints[i] = points[i]
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
            drawingPoints[i + 2] = points[i]
            drawingPoints[i + 3] = points[i + 3] + (pointsFrom[i + 3] - points[i + 3]) * animValue
        }
    }

    override fun alphaFromAnimValue(v: Float) = 1f

    private inline fun setupPaint() {
        val s = (innerTimeIndexStart + (innerTimeIndexEnd - innerTimeIndexStart) / 2) * 4
        strokeWidth = drawingPoints[s + 4] - drawingPoints[s]
        chartsData.barHalfWidth = strokeWidth / 2
        paints.paintChartLine.strokeWidth = strokeWidth
        paints.paintChartLine.color = chartData.color
    }

    private inline fun drawingPointsSize() = innerTimeIndexEnd - innerTimeIndexStart

    private inline fun y(pointValue: Int) = mh - kY * pointValue

    private inline fun y0(index: Int): Int {
        var y0 = 0
        for (k in 0 until chartIndex) y0 += if (columns[k].enabled) columns[k].values[index] else 0
        return y0
    }
}