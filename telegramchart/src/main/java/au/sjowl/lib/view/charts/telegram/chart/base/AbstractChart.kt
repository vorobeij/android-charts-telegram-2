package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

abstract class AbstractChart(
    val chartData: ChartData,
    val chartsData: ChartsData,
    val chartLayoutParams: ChartLayoutParams
) {

    protected open lateinit var paints: ChartPaints

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

    protected var toDrawPointer: Boolean = false

    open fun updateTheme(context: Context) {
        paints = ChartPaints(context)
    }

    fun updatePoints() {
        setVals()
        calculatePoints()
        onAnimateValues(0f)
    }

    fun onAnimationStart() {
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

    open fun onTimeAnimationStart() {
        timeIndexStart = 0
        timeIndexEnd = chartsData.size - 1

        innerTimeIndexStart = 0
        innerTimeIndexEnd = chartsData.size - 1

        calculatePoints()
        fixPointsFrom()
        innerTimeIndexStart = timeIndexStart()
        innerTimeIndexEnd = timeIndexEnd()
    }

    open fun onTimeAnimation(v: Float) {
        timeIndexStart = timeIndexStart()
        timeIndexEnd = timeIndexEnd()

        calculateInnerBorders()
        kX = kx()
        kY = ky()

        calculatePoints()
        animValue = v
        updateOnAnimation()
    }

    open fun draw(canvas: Canvas) {
        if (chartData.enabled || enabled) {
            paints.paintChartLine.color = chartData.color
            paints.paintChartLine.alpha = (alpha * 255).toInt()
            onDraw(canvas)
        }
    }

    /**
     * Draw pointer above drawn chart (for lines - circles, for stack - transparent lines)
     */
    open fun drawPointer(canvas: Canvas) = Unit

    fun onDrawPointer(draw: Boolean) {
        toDrawPointer = draw
    }

    fun getPointerX(): Float = x(chartsData.pointerTimeIndex)

    fun updateInnerBounds() {
        setVals()
    }

    protected abstract fun onDraw(canvas: Canvas)

    protected open fun calculateInnerBorders() {
        // right points
        var x = 0f
        innerTimeIndexEnd = timeIndexEnd()
        while (x < chartLayoutParams.w && innerTimeIndexEnd < chartsData.times.size - 1) {
            x = x(innerTimeIndexEnd++)
        }
        // left points
        innerTimeIndexStart = timeIndexStart()
        while (innerTimeIndexStart > 0 && x >= 0) {
            x = x(innerTimeIndexStart--)
        }
        chartsData.innerTimeIndexStart = innerTimeIndexStart
        chartsData.innerTimeIndexEnd = innerTimeIndexEnd
    }

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

    protected inline fun x(index: Int) = kX * (chartsData.times[index] - chartsData.times[timeIndexStart]) + chartLayoutParams.paddingHorizontal

    protected inline fun kx() = w / (chartsData.times[timeIndexEnd] - chartsData.times[timeIndexStart])

    protected open fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.windowValueInterval

    protected inline fun setVals() {
        w = chartLayoutParams.w - 2f * chartLayoutParams.paddingHorizontal
        h = chartLayoutParams.h
        timeIndexStart = timeIndexStart()
        timeIndexEnd = timeIndexEnd()
        mh = h - chartLayoutParams.paddingBottom
        kX = kx()
        kY = ky()

        calculateInnerBorders()
    }

    protected open fun timeIndexStart() = chartsData.timeIndexStart // todo make it property

    protected open fun timeIndexEnd() = chartsData.timeIndexEnd // todo make it property

    open class ChartPaints(context: Context) : BasePaints(context) {
        open val paintChartLine = antiAliasPaint().apply {
            strokeWidth = dimensions.chartLineWidth
            style = Paint.Style.STROKE
        }

        open val paintTint = simplePaint().apply {
            color = colors.scrollBackground
            style = Paint.Style.STROKE
        }

        open val paintPointerCircle = antiAliasPaint().apply {
            style = Paint.Style.FILL
            color = colors.background
        }

        val title = antiAliasPaint().apply {
            textSize = dimensions.pieTitle
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            color = colors.pieTitle
        }
    }
}