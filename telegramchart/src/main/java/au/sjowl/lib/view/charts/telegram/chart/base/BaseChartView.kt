package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ChartAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.other.SLog
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

abstract class BaseChartView : View, ThemedView {

    open var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            charts.clear()
            value.columns.values.forEach {
                charts.add(provideChart(it, value).apply {
                    updateTheme(context)
                })
            }
            chartsData.scaleInProgress = false
            updateCharts()
        }

    protected val charts = arrayListOf<AbstractChart>()

    protected open val chartLayoutParams = ChartLayoutParams(context)

    private val animator = ChartAnimatorWrapper(
        onStart = {
            calcExtremums()
            charts.forEach { it.onAnimationStart() }
        },
        onAnimate = { value ->
            charts.forEach { chart -> chart.onAnimateValues(value) }
            invalidate()
        })

    abstract fun calcExtremums()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updateCharts()
    }

    override fun onDraw(canvas: Canvas) {
        for (i in charts.size - 1 downTo 0) {
            charts[i].draw(canvas)
        }
    }

    override fun updateTheme() {
        charts.forEach { it.updateTheme(context) }
    }

    fun updateCharts() {
        chartLayoutParams.w = measuredWidth * 1f
        chartLayoutParams.h = measuredHeight * 1f
        if (measuredWidth > 0 && measuredHeight > 0) {
            charts.firstOrNull()?.updateInnerBounds()
            calcExtremums()
            charts.forEach { it.updatePoints() }
            invalidate()
        }
    }

    fun onDrawPointer(draw: Boolean) = charts.forEach { it.onDrawPointer(draw) }

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
        updateCharts()
    }

    protected abstract fun provideChart(it: ChartData, value: ChartsData): AbstractChart

    init {
        SLog.d("${this.javaClass.simpleName} created")
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}