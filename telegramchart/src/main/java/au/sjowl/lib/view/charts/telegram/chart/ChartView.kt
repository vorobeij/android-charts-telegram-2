package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.AnimView
import au.sjowl.lib.view.charts.telegram.BaseSurfaceView
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class ChartView : BaseSurfaceView, ThemedView, AnimView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            charts.clear()
            axisY.chartsData = value
            value.columns.values.forEach { charts.add(Chart(it, chartLayoutParams, paints, value)) }
            chartsData.scaleInProgress = false

            onTimeIntervalChanged()
            invalidate()
        }

    var drawPointer = false

    private val charts = arrayListOf<Chart>()

    private val chartLayoutParams = ChartLayoutParams(context)

    private var paints = ChartPaints(context, ChartColors(context))

    private val axisY = AxisY(chartLayoutParams, paints, chartsData)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartLayoutParams.w = measuredWidth * 1f
        chartLayoutParams.h = measuredHeight * 1f
        onTimeIntervalChanged()
    }

    override fun drawSurface(canvas: Canvas) {
        drawSelf(canvas)
    }

    override fun updateTheme(colors: ChartColors) {
        paints = ChartPaints(context, colors)
        axisY.paints = paints
        charts.forEach { it.paints = paints }

        invalidate()
    }

    override fun updateFinishState() {
        adjustValueRange()
        axisY.onAnimateValues(0f)
        charts.forEach { it.updateFinishState() }
    }

    override fun updateStartPoints() {
        axisY.updateStartPoints()
        charts.forEach { it.updateStartPoints() }
    }

    override fun onAnimateValues(v: Float) {
        axisY.onAnimateValues(v)
        charts.forEach { it.onAnimateValues(v) }
        invalidate()
    }

    fun updateTimeIndexFromX(x: Float) {
        val xx = x
        val w = measuredWidth
        if (charts.size > 0) {
            chartsData.pointerTimeIndex = chartsData.timeIndexStart + (chartsData.timeIntervalIndexes * xx / w).toInt()
            chartsData.pointerTimeX = charts[0].getPointerX()
        }
    }

    fun onTimeIntervalChanged() {
        adjustValueRange()
        axisY.onAnimateValues(0f)
        charts.forEach { it.setupPoints() }
        invalidate()
    }

    private fun drawSelf(canvas: Canvas) {
        canvas.drawColor(paints.colors.background)
        charts.forEach { it.draw(canvas) }
        axisY.drawGrid(canvas)
        axisY.drawMarks(canvas)
        if (drawPointer) {
            paints.paintGrid.alpha = 25
            canvas.drawLine(chartsData.pointerTimeX, chartLayoutParams.h, chartsData.pointerTimeX, chartLayoutParams.paddingTop.toFloat(), paints.paintGrid)
            charts.forEach { it.drawPointer(canvas) }
        }
    }

    private fun adjustValueRange() {
        val columns = chartsData.columns.values
        columns.forEach { it.calculateBorders(chartsData.timeIndexStart, chartsData.timeIndexEnd) }
        val enabled = columns.filter { it.enabled }
        val chartsMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
        val chartsMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100
        axisY.adjustValuesRange(chartsMin, chartsMax)
    }

    private fun init() {
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}