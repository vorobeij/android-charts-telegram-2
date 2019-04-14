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

    protected var points = FloatArray(chartData.values.size * 2)

    protected var pointsFrom = FloatArray(chartData.values.size * 2)

    protected var drawingPoints = FloatArray(chartData.values.size * 2)

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paints.paintChartLine)
    }

    override fun calculatePoints() {
        var j = 0
        for (i in innerTimeIndexStart..innerTimeIndexEnd) {
            j = i shl 1
            points[j] = x(i)
            points[j + 1] = y(i)
        }
    }

    override fun fixPointsFrom() {
        points.copyInto(pointsFrom)
        enabled = chartData.enabled
    }

    override fun updateOnAnimation() {
        for (i in (innerTimeIndexStart shl 1)..(innerTimeIndexEnd shl 1) step 2) {
            drawingPoints[i] = x(i shr 1)
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
        }
        updatePathFromPoints()
    }

    override fun drawPointer(canvas: Canvas) {
        if (!chartData.enabled) return
        paints.paintChartLine.color = chartData.color
        val i = chartsData.pointerTimeIndex
        val x = x(i)
        val y = y(i)
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintPointerCircle)
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintChartLine)
    }

    open fun updatePathFromPoints() {
        with(path) {
            reset()
            if (drawingPoints.size > 1) {
                val start = 2 * innerTimeIndexStart
                val end = 2 * innerTimeIndexEnd
                moveTo(drawingPoints[start], drawingPoints[start + 1])
                for (i in (start + 2)..end step 2) {
                    lineTo(drawingPoints[i], drawingPoints[i + 1])
                }
            }
        }
    }

    protected open fun y(index: Int) = mh - kY * (chartData.values[index] - chartsData.windowMin)
}
