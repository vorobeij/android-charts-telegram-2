package au.sjowl.lib.view.charts.telegram.chart.chartview.chart

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.chart.chartview.BaseChartView
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

abstract class AbstractChart(
    var chartData: ChartData,
    var paints: BaseChartView.ChartViewPaints,
    val chartLayoutParams: ChartLayoutParams,
    val chartsData: ChartsData
) {

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

    abstract fun onDraw(canvas: Canvas)

    abstract fun fixPointsFrom()

    abstract fun updateOnAnimation()

    abstract fun calculatePoints()

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

    open fun alphaFromAnimValue(v: Float): Float {
        return when {
            chartData.enabled && enabled -> 1f
            chartData.enabled && !enabled -> 1f - v
            !chartData.enabled && !enabled -> 0f
            else -> v
        }
    }

    fun draw(canvas: Canvas) {
        if (chartData.enabled || enabled) {
            paints.paintChartLine.color = chartData.color
            paints.paintChartLine.alpha = (alpha * 255).toInt()
            onDraw(canvas)
        }
    }

    open fun drawPointer(canvas: Canvas) = Unit

    fun getPointerX(): Float = x(chartsData.pointerTimeIndex)

    protected fun x(index: Int) = kX * (chartsData.time.values[index] - chartsData.time.values[timeIndexStart]) + chartLayoutParams.paddingHorizontal

    protected fun kx() = w / (chartsData.time.values[timeIndexEnd] - chartsData.time.values[timeIndexStart])

    protected open fun ky() = 1f * (h - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / chartsData.valueInterval

    protected inline fun setVals() {
        w = chartLayoutParams.w - 2 * chartLayoutParams.paddingHorizontal
        h = chartLayoutParams.h
        timeIndexStart = chartsData.timeIndexStart
        timeIndexEnd = chartsData.timeIndexEnd
        mh = h - chartLayoutParams.paddingBottom
        kX = kx()
        kY = ky()

        calculateInnerBorders()
    }

    protected open fun calculateInnerBorders() {
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

    protected fun setPreAnimVals() {
        timeIndexStart = chartsData.timeIndexStart
        timeIndexEnd = chartsData.timeIndexEnd
        calculateInnerBorders()
    }
}