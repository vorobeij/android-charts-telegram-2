package au.sjowl.lib.view.charts.telegram.chart

import android.graphics.Canvas
import android.graphics.Path
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class Chart(
    val data: ChartData,
    val chartLayoutParams: ChartLayoutParams,
    var paints: ChartPaints,
    val chartsData: ChartsData
) {

    protected val path = Path()

    protected var enabled = data.enabled

    protected var alpha = 1f

    private val points = FloatArray(data.values.size * 2)

    private val pointsFrom = FloatArray(data.values.size * 2)

    private val drawingPoints = FloatArray(data.values.size * 2)

    private var innerTimeIndexStart = 0

    private var innerTimeIndexEnd = 0

    private var w = 0f

    private var h = 0f

    private var timeIndexStart = 0

    private var timeIndexEnd = 0

    private var mh = 0f

    private var kX = 0f

    private var kY = 0f

    private var animValue = 0f

    fun setupPoints() {
        calculatePoints()
        for (i in 2 * innerTimeIndexStart..(2 * innerTimeIndexEnd + 1)) {
            drawingPoints[i] = points[i]
        }
        updatePathFromPoints()
    }

    fun updateStartPoints() {
        for (i in 2 * innerTimeIndexStart..(2 * innerTimeIndexEnd + 1)) {
            pointsFrom[i] = drawingPoints[i]
        }
        enabled = data.enabled
    }

    fun updateFinishState() {
        calculatePoints()
    }

    fun onAnimateValues(v: Float) {
        alpha = when {
            data.enabled && enabled -> 1f
            data.enabled && !enabled -> 1f - v
            !data.enabled && !enabled -> 0f
            else -> v
        }
        animValue = v
        for (i in 2 * innerTimeIndexStart..2 * innerTimeIndexEnd step 2) {
            drawingPoints[i] = points[i]
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * v
        }
        updatePathFromPoints()
    }

    fun draw(canvas: Canvas) {
        if (data.enabled || enabled) {
            val paint = paints.paintChartLine
            paint.color = data.color
            paint.alpha = (alpha * 255).toInt()
            canvas.drawPath(path, paint)
        }
    }

    fun drawPointer(canvas: Canvas) {
        if (!data.enabled) return
        paints.paintChartLine.color = data.color
        val i = chartsData.pointerTimeIndex
        val x = x(i)
        val y = y(i)
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintPointerCircle)
        canvas.drawCircle(x, y, chartLayoutParams.pointerCircleRadius, paints.paintChartLine)
    }

    fun getPointerX(): Float {
        setVals()
        return x(chartsData.pointerTimeIndex)
    }

    private inline fun calculatePoints() {
        setVals()
        var j = 0
        for (i in innerTimeIndexStart..innerTimeIndexEnd) {
            j = i * 2
            points[j] = x(i)
            points[j + 1] = y(i)
        }
    }

    private fun updatePathFromPoints() {
        with(path) {
            reset()
            if (drawingPoints.size > 1) {
                val start = 2 * innerTimeIndexStart
                moveTo(drawingPoints[start], drawingPoints[start + 1])
                for (i in (start + 2)..2 * innerTimeIndexEnd step 2) {
                    lineTo(drawingPoints[i], drawingPoints[i + 1])
                }
            }
        }
    }

    private inline fun x(index: Int) = kX * (chartsData.time.values[index] - chartsData.time.values[timeIndexStart]) + chartLayoutParams.paddingHorizontal

    private inline fun y(index: Int) = mh - kY * (data.values[index] - chartsData.valueMin)

    private inline fun setVals() {
        w = chartLayoutParams.w - 2 * chartLayoutParams.paddingHorizontal
        h = chartLayoutParams.h
        timeIndexStart = chartsData.timeIndexStart
        timeIndexEnd = chartsData.timeIndexEnd
        mh = h - chartLayoutParams.paddingBottom
        kX = w / (chartsData.time.values[timeIndexEnd] - chartsData.time.values[timeIndexStart])
        kY = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.valueInterval

        // right points
        var x = 0f
        innerTimeIndexEnd = timeIndexEnd
        while (x < chartLayoutParams.w + chartLayoutParams.paddingHorizontal && innerTimeIndexEnd < chartsData.time.values.size - 1) {
            x = x(innerTimeIndexEnd++)
        }
        // left points
        innerTimeIndexStart = timeIndexStart
        while (innerTimeIndexStart > 0 && x > -chartLayoutParams.paddingHorizontal) {
            x = x(innerTimeIndexStart--)
        }
    }
}
