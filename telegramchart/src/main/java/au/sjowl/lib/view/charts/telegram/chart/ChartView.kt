package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.AnimView
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class ChartView : View, ThemedView, AnimView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            charts.clear()
            value.columns.values.forEach { charts.add(Chart(it, chartLayoutParams, paints, value)) }
            chartsData.scaleInProgress = false

            onTimeIntervalChanged()
        }

    var drawPointer = false

    private val charts = arrayListOf<Chart>()

    private val chartLayoutParams = ChartLayoutParams(context)

    private var paints = ChartPaints(context, ChartColors(context))

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        chartLayoutParams.w = measuredWidth * 1f
        chartLayoutParams.h = measuredHeight * 1f
        onTimeIntervalChanged()
    }

    override fun onDraw(canvas: Canvas) {
        charts.forEach { it.draw(canvas) }
        if (drawPointer) {
            paints.paintGrid.alpha = 25
            canvas.drawLine(chartsData.pointerTimeX, chartLayoutParams.h, chartsData.pointerTimeX, chartLayoutParams.paddingTop.toFloat(), paints.paintGrid)
            charts.forEach { it.drawPointer(canvas) }
        }
    }

    override fun updateTheme(colors: ChartColors) {
        paints = ChartPaints(context, colors)
        charts.forEach { it.paints = paints }

        invalidate()
    }

    override fun updateFinishState() {
        charts.forEach { it.updateFinishState() }
    }

    override fun updateStartPoints() {
        charts.forEach { it.updateStartPoints() }
    }

    override fun onAnimateValues(v: Float) {
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
        charts.forEach { it.setupPoints() }
        invalidate()
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