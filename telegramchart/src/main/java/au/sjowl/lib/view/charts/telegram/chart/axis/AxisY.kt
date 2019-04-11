package au.sjowl.lib.view.charts.telegram.chart.axis

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class AxisY : View, ThemedView {

    open var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axises = if (chartsData.isYScaled)
                arrayListOf(
                    AxisLeft(chartLayoutParams.yMarks, context, chartLayoutParams, chartsData.charts.first()),
                    AxisRight(chartLayoutParams.yMarks, context, chartLayoutParams, chartsData.charts.last())
                )
            else arrayListOf(AxisVert(chartLayoutParams.yMarks, context, chartLayoutParams))
        }

    protected val chartLayoutParams: ChartLayoutParams = ChartLayoutParams(context)

    protected var axises = arrayListOf<AxisVert>()

    private val animator = object : ValueAnimatorWrapper({ value ->
        onAnimateValues(value)
        invalidate()
    }) {
        override fun start() {
            axises.forEach { it.beforeAnimate(chartsData) }
            adjustValuesRange()
            super.start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartLayoutParams.w = w * 1f
        chartLayoutParams.h = h * 1f
        onAnimateValues(0f)
    }

    override fun onDraw(canvas: Canvas) {
        if (measuredHeight == 0 || measuredWidth == 0) return
        axises[0].drawGrid(canvas, measuredWidth, measuredHeight)
        axises.forEach { it.drawMarks(canvas) }
    }

    override fun updateTheme() {
        axises.forEach { it.updateTheme() }
    }

    fun onAnimateValues(v: Float) { // v: 1 -> 0
        if (measuredHeight == 0 || measuredWidth == 0) return
        axises.forEach { it.onAnimate(v, measuredHeight) }
        invalidate()
    }

    open fun adjustValuesRange() {
        if (chartsData.isYScaled) {
            var chart = chartsData.charts[0]
            axises[0].calculateMarks(chart.windowMin, chart.windowMax)
            chart = chartsData.charts[1]
            axises[1].calculateMarks(chart.windowMin, chart.windowMax)
        } else {
            axises[0].calculateMarks(chartsData.windowMin, chartsData.windowMax)
        }
    }

    fun anim() {
        if (!chartsData.isYScaled)
            animator.start()
    }

    fun onTimeIntervalChanged() {
        onAnimateValues(0f)
    }

    private fun init() {
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}