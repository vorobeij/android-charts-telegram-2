package au.sjowl.lib.view.charts.telegram.chart.linear

import android.graphics.Canvas
import android.graphics.Path
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class LinearChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : AbstractChart(chartData, chartsData, chartLayoutParams) {

    protected val path = Path()

    protected var points = FloatArray(chartData.values.size shl 1)

    protected var pointsFrom = FloatArray(chartData.values.size shl 1)

    protected var drawingPoints = FloatArray(chartData.values.size shl 1)

    override fun onDraw(canvas: Canvas) {
        canvas.drawLines(drawingPoints, innerTimeIndexStart * 2, (innerTimeIndexEnd - innerTimeIndexStart) * 2, paints.paintChartLine)
        canvas.drawLines(drawingPoints, innerTimeIndexStart * 2 + 2, (innerTimeIndexEnd - innerTimeIndexStart) * 2 - 2, paints.paintChartLine)
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
        points.copyInto(pointsFrom)
    }

    override fun updateOnAnimation() {
        for (i in (innerTimeIndexStart * 2)..(innerTimeIndexEnd * 2) step 2) {
            drawingPoints[i] = x(i / 2)
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
        }
    }

    override fun drawPointer(canvas: Canvas) {
        paints.paintChartLine.color = chartData.color
        paints.paintChartLine.alpha = (alpha * 255).toInt()
        paints.paintPointerCircle.alpha = (alpha * 255).toInt()
        val i = chartsData.pointerTimeIndex shl 1
        val x = drawingPoints[i]
        val y = drawingPoints[i + 1]
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintPointerCircle)
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintChartLine)
    }

    protected open fun y(index: Int) = mh - kY * (chartData.values[index] - chartsData.windowMin)
}
