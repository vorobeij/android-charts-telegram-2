package au.sjowl.lib.view.charts.telegram.chart.chartview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.chart.chartview.chart.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.chartview.chart.LineChart
import au.sjowl.lib.view.charts.telegram.chart.chartview.chart.LineChartYScaled
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class BaseChartView : View, ThemedView {

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

    var drawPointer = false

    protected val charts = arrayListOf<AbstractChart>()

    protected val chartLayoutParams = ChartLayoutParams(context)

    protected var paints = ChartViewPaints(context)

    private val animator = object : ValueAnimatorWrapper({ value ->
        charts.forEach { chart -> chart.onAnimateValues(value) }
        invalidate()
    }) {
        override fun start() {
            charts.forEach { it.onAnimationStart() }
            super.start()
        }
    }

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

    override fun updateTheme() {
        paints = ChartViewPaints(context)
        charts.forEach { it.paints = paints }

        invalidate()
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

    protected open fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return if (chartsData.isYScaled) // todo move yscaled to separate cahrt type
            LineChartYScaled(it, paints, chartLayoutParams, value)
        else
            LineChart(it, paints, chartLayoutParams, value)
    }

    class ChartViewPaints(context: Context) : BasePaints(context) {
        val paintGrid = paint().apply {
            color = colors.gridLines
            style = Paint.Style.STROKE
            strokeWidth = dimensions.gridWidth
            strokeCap = Paint.Cap.ROUND
        }

        val paintChartLine = paint().apply {
            strokeWidth = 6f
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