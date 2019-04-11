package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

abstract class BaseChartView : View, ThemedView {

    open var chartsData: ChartsData = ChartsData()
        set(value) {

            // todo animate changes
            // todo move to setChartsData() and override properly

            field = value
            charts.clear()
            value.columns.values.forEach {
                charts.add(provideChart(it, value))
            }
            chartsData.scaleInProgress = false

            onTimeIntervalChanged()
        }

    protected val charts = arrayListOf<AbstractChart>()

    protected open val chartLayoutParams = ChartLayoutParams(context)

    protected open var paints = ChartViewPaints(context)

    private val animator = object : ValueAnimatorWrapper({ value ->
        charts.forEach { chart -> chart.onAnimateValues(value) }
        invalidate()
    }) {
        override fun start() {
            charts.forEach { it.onAnimationStart() }
            calcExtremums()
            super.start()
        }
    }

    abstract fun calcExtremums()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartLayoutParams.w = measuredWidth * 1f
        chartLayoutParams.h = measuredHeight * 1f
        onTimeIntervalChanged()
    }

    override fun onDraw(canvas: Canvas) {
        charts.forEach { it.draw(canvas) }
    }

    override fun updateTheme() {
        paints = ChartViewPaints(context)
        charts.forEach { it.paints = paints }
    }

    fun drawPointers(canvas: Canvas) {
        charts.forEach { it.drawPointer(canvas) }
    }

    open fun onChartStateChanged() {
        animator.start()
    }

    open fun updateTimeIndexFromX(x: Float) {
        val xx = x
        val w = measuredWidth
        if (charts.size > 0 && w > 0) {
            chartsData.pointerTimeIndex = chartsData.timeIndexStart + (chartsData.timeIntervalIndexes * xx / w).toInt()
            chartsData.pointerTimeX = charts[0].getPointerX()
        }
    }

    open fun onTimeIntervalChanged() {
        charts.forEach { it.updatePoints() }
        invalidate()
    }

    protected abstract fun provideChart(it: ChartData, value: ChartsData): AbstractChart

    class ChartViewPaints(context: Context) : BasePaints(context) {
        val paintChartLine = Paint().apply {
            strokeWidth = dimensions.chartLineWidth
            style = Paint.Style.STROKE
        }

        val paintPointerCircle = paint().apply {
            style = Paint.Style.FILL
            color = colors.background
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}