package au.sjowl.lib.view.charts.telegram.chart.chart

import android.graphics.Canvas
import android.graphics.Path
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class Chart(
    chartData: ChartData,
    var paints: ChartView.ChartViewPaints,
    val chartLayoutParams: ChartLayoutParams,
    val chartsData: ChartsData
) {

    var chartData: ChartData = chartData

    protected val path = Path()

    protected var enabled = chartData.enabled

    protected var alpha = 1f

    protected var mh = 0f

    protected var kY = 0f

    protected var h = 0f

    private val points = FloatArray(chartData.values.size * 2)

    private val pointsFrom = FloatArray(chartData.values.size * 2)

    private val drawingPoints = FloatArray(chartData.values.size * 2)

    private var innerTimeIndexStart = 0

    private var innerTimeIndexEnd = 0

    private var w = 0f

    private var timeIndexStart = 0

    private var timeIndexEnd = 0

    private var kX = 0f

    private var animValue = 0f

    fun updatePoints() {
        setVals()
        calculatePoints()
        onAnimateValues(0f)
    }

    fun updateStartPoints() {
        setPreAnimVals()
        calculatePoints()
        for (i in 2 * innerTimeIndexStart..(2 * innerTimeIndexEnd + 1)) {
            pointsFrom[i] = points[i]
        }
        enabled = chartData.enabled

        setVals()
        calculatePoints()
    }

    fun onAnimateValues(v: Float) {
        alpha = when {
            chartData.enabled && enabled -> 1f
            chartData.enabled && !enabled -> 1f - v
            !chartData.enabled && !enabled -> 0f
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
        if (chartData.enabled || enabled) {
            val paint = paints.paintChartLine
            paint.color = chartData.color
            paint.alpha = (alpha * 255).toInt()
            canvas.drawPath(path, paint)
        }
    }

    fun drawPointer(canvas: Canvas) {
        if (!chartData.enabled) return
        paints.paintChartLine.color = chartData.color
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

    protected open fun y(index: Int) = mh - kY * (chartData.values[index] - chartsData.valueMin)

    protected open fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.valueInterval

    private inline fun calculatePoints() {
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
                val end = 2 * innerTimeIndexEnd
                moveTo(drawingPoints[start], drawingPoints[start + 1])
                for (i in (start + 2)..end step 2) {
                    lineTo(drawingPoints[i], drawingPoints[i + 1])
                }
            }
        }
    }

    private inline fun x(index: Int) = kX * (chartsData.time.values[index] - chartsData.time.values[timeIndexStart]) + chartLayoutParams.paddingHorizontal

    private inline fun setVals() {
        w = chartLayoutParams.w - 2 * chartLayoutParams.paddingHorizontal
        h = chartLayoutParams.h
        timeIndexStart = chartsData.timeIndexStart
        timeIndexEnd = chartsData.timeIndexEnd
        mh = h - chartLayoutParams.paddingBottom
        kX = w / (chartsData.time.values[timeIndexEnd] - chartsData.time.values[timeIndexStart])
        kY = ky()

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

    private fun setPreAnimVals() {
        timeIndexStart = chartsData.timeIndexStart
        timeIndexEnd = chartsData.timeIndexEnd
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

class ChartYScaled(
    chartData: ChartData,
    paints: ChartView.ChartViewPaints,
    chartLayoutParams: ChartLayoutParams,
    chartsData: ChartsData
) : Chart(chartData, paints, chartLayoutParams, chartsData) {
    override fun y(index: Int) = mh - kY * (chartData.values[index] - chartData.windowMin)
    override fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / (chartData.windowMax - chartData.windowMin)
}
