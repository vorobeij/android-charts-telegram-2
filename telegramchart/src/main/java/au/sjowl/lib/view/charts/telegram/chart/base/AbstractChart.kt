package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

abstract class AbstractChart(
    val chartData: ChartData,
    val chartsData: ChartsData,
    val chartLayoutParams: ChartLayoutParams
) {

    protected open lateinit var paints: ChartPaints // = ChartPaints(context)

    protected var timeIndexStart = 0

    protected var kX = 0f

    protected var w = 0f

    protected var timeIndexEnd = 0

    protected var innerTimeIndexStart = 0

    protected var innerTimeIndexEnd = 0

    protected var mh = 0f

    protected var kY = 0f

    protected var h = 0f

    protected var enabled = chartData.enabled

    protected var alpha = 1f

    protected var animValue = 0f

    open fun updateTheme(context: Context) {
        paints = ChartPaints(context)
    }

    fun updatePoints() {
        setVals()
        calculatePoints()
        onAnimateValues(0f) // todo need?
    }

    fun onAnimationStart() {
        setPreAnimVals()
        calculatePoints()
        fixPointsFrom()
        setVals()
        calculatePoints()
    }

    fun onAnimateValues(v: Float) {
        alpha = alphaFromAnimValue(v)
        animValue = v
        updateOnAnimation()
        if (v == 0f) {
            enabled = chartData.enabled
        }
    }

    fun draw(canvas: Canvas) {
        if (chartData.enabled || enabled) {
            paints.paintChartLine.color = chartData.color
            paints.paintChartLine.alpha = (alpha * 255).toInt()
            onDraw(canvas)
        }
    }

    /**
     * Draw pointer above drawn chart
     */
    open fun drawPointer(canvas: Canvas) = Unit

    fun getPointerX(): Float = x(chartsData.pointerTimeIndex)

    protected abstract fun onDraw(canvas: Canvas)

    protected abstract fun fixPointsFrom()

    protected abstract fun updateOnAnimation()

    protected abstract fun calculatePoints()

    protected open fun alphaFromAnimValue(v: Float): Float {
        return when {
            chartData.enabled && enabled -> 1f
            chartData.enabled && !enabled -> 1f - v
            !chartData.enabled && !enabled -> 0f
            else -> v
        }
    }

    protected fun x(index: Int) = kX * (chartsData.times[index] - chartsData.times[timeIndexStart]) + chartLayoutParams.paddingHorizontal

    protected fun kx() = w / (chartsData.times[timeIndexEnd] - chartsData.times[timeIndexStart])

    protected open fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.windowValueInterval

    protected inline fun setVals() {
        w = chartLayoutParams.w - 2 * chartLayoutParams.paddingHorizontal
        h = chartLayoutParams.h
        timeIndexStart = timeIndexStart()
        timeIndexEnd = timeIndexEnd()
        mh = h - chartLayoutParams.paddingBottom
        kX = kx()
        kY = ky()

        calculateInnerBorders()
    }

    protected open fun timeIndexStart() = chartsData.timeIndexStart

    protected open fun timeIndexEnd() = chartsData.timeIndexEnd

    protected open fun calculateInnerBorders() {
        // right points
        var x = 0f
        innerTimeIndexEnd = timeIndexEnd
        while (x < chartLayoutParams.w + chartLayoutParams.paddingHorizontal && innerTimeIndexEnd < chartsData.times.size - 1) {
            x = x(innerTimeIndexEnd++)
        }
        // left points
        innerTimeIndexStart = timeIndexStart()
        while (innerTimeIndexStart > 0 && x > -chartLayoutParams.paddingHorizontal) {
            x = x(innerTimeIndexStart--)
        }
    }

    protected fun setPreAnimVals() {
        timeIndexStart = timeIndexStart()
        timeIndexEnd = timeIndexEnd()
        calculateInnerBorders()
    }

    open class ChartPaints(context: Context) : BasePaints(context) {
        open val paintChartLine = Paint().apply {
            strokeWidth = dimensions.chartLineWidth
            style = Paint.Style.STROKE
        }

        open val paintPointerCircle = paint().apply {
            style = Paint.Style.FILL
            color = colors.background
        }
    }
}