package au.sjowl.lib.view.charts.telegram.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import au.sjowl.lib.view.charts.telegram.BaseSurfaceView
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class ChartView : BaseSurfaceView, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            charts.clear()
            axisY.chartsData = value
            pointer.chartData = value
            value.columns.values.forEach { charts.add(Chart(it, chartLayoutParams, paints, value)) }
            chartsData.scaleInProgress = false
        }

    private val charts = arrayListOf<Chart>()

    private val chartLayoutParams = ChartLayoutParams(context)

    private var paints = ChartPaints(context, ChartColors(context))

    private val pointer = ChartPointerPopup(context, paints)

    private val axisY = AxisY(chartLayoutParams, paints, chartsData)

    private var onDrawPointer: ((x: Float, measuredWidth: Int) -> Unit) = pointer::updatePoints

    private var drawPointer = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartLayoutParams.w = measuredWidth * 1f
        chartLayoutParams.h = measuredHeight * 1f
        onTimeIntervalChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                drawPointer = true
                updateTimeIndexFromX(event.x)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                updateTimeIndexFromX(event.x)
                invalidate()
            }
        }
        return true
    }

    override fun drawSurface(canvas: Canvas) {
        drawSelf(canvas)
    }

    override fun updateTheme(colors: ChartColors) {
        paints = ChartPaints(context, colors)
        axisY.paints = paints
        pointer.paints = paints
        charts.forEach { it.paints = paints }
        invalidate()
    }

    fun onTimeIntervalChanged() {
        drawPointer = false
        adjustValueRange()
        axisY.onAnimateValues(0f)
        charts.forEach { it.setupPoints() }
        invalidate()
    }

    fun updateFinishState() {
        adjustValueRange()
        axisY.onAnimateValues(0f)
        charts.forEach { it.updateFinishState() }
        pointer.update()
    }

    fun updateStartPoints() {
        axisY.updateStartPoints()
        charts.forEach { it.updateStartPoints() }
    }

    fun onAnimateValues(v: Float) {
        axisY.onAnimateValues(v)
        charts.forEach { it.onAnimateValues(v) }
        invalidate()
    }

    private fun drawSelf(canvas: Canvas) {
        canvas.drawColor(paints.colors.background)
        axisY.drawGrid(canvas)
        charts.forEach { it.draw(canvas) }
        axisY.drawMarks(canvas)
        if (drawPointer) {
            paints.paintGrid.alpha = 255
            canvas.drawLine(chartsData.pointerTimeX, chartLayoutParams.h, chartsData.pointerTimeX, chartLayoutParams.paddingTop.toFloat(), paints.paintGrid)
            charts.forEach { it.drawPointer(canvas) }
            pointer.draw(canvas)
        }
    }

    private fun adjustValueRange() {
        val columns = chartsData.columns.values
        columns.forEach { it.calculateBorders(chartsData.timeIndexStart, chartsData.timeIndexEnd) }
        val enabled = columns.filter { it.enabled }
        val chartsMin = enabled.minBy { it.min }?.min ?: 0
        val chartsMax = enabled.maxBy { it.max }?.max ?: 100
        axisY.adjustValuesRange(chartsMin, chartsMax)
    }

    private inline fun updateTimeIndexFromX(x: Float) {
        val t = chartsData.pointerTimeIndex

        val p = chartLayoutParams.paddingHorizontal
        val xx = x - p
        val w = measuredWidth - 2 * p
        if (charts.size > 0) {
            chartsData.pointerTimeIndex = chartsData.timeIndexStart + (chartsData.timeIntervalIndexes * xx / w).toInt()
            chartsData.pointerTimeX = charts[0].getPointerX()
        }

        if (t != chartsData.pointerTimeIndex) {
            onDrawPointer.invoke(xx, w)
            invalidate()
        }
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