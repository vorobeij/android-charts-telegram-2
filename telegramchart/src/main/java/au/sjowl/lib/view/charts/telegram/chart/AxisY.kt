package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class AxisY : View, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axises = if (chartsData.isYScaled)
                arrayListOf(
                    AxisLeft(chartLayoutParams.yMarks, context, chartLayoutParams, chartsData.columns.values.first()),
                    AxisRight(chartLayoutParams.yMarks, context, chartLayoutParams, chartsData.columns.values.last())
                )
            else arrayListOf(AxisVert(chartLayoutParams.yMarks, context, chartLayoutParams))
        }

    private val chartLayoutParams: ChartLayoutParams = ChartLayoutParams(context)

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

    private var axises = arrayListOf<AxisVert>()

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

    fun adjustValuesRange() {
        val columns = chartsData.columns.values
        columns.forEach { it.calculateBorders(chartsData.timeIndexStart, chartsData.timeIndexEnd) }
        if (!chartsData.isYScaled) {
            val enabled = columns.filter { it.enabled }
            val chartsMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
            val chartsMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100
            chartsData.valueMin = chartsMin
            chartsData.valueMax = chartsMax
            axises[0].calculateMarks(chartsMin, chartsMax)
        } else {
            var chart = chartsData.columns.values.first()
            axises[0].calculateMarks(chart.windowMin, chart.windowMax)
            chart = chartsData.columns.values.last()
            axises[1].calculateMarks(chart.windowMin, chart.windowMax)
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