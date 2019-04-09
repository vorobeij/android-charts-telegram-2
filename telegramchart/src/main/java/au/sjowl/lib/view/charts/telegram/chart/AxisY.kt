package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class AxisY : View, ThemedView {

    var paints = ChartPaints(context, ChartColors(context))

    var chartsData: ChartsData = ChartsData()

    private val chartLayoutParams: ChartLayoutParams = ChartLayoutParams(context)

    private val valueFormatter = ValueFormatter()

    private var pointsFrom = arrayListOf<CanvasPoint>().apply {
        repeat(chartLayoutParams.yMarks + 1) {
            add(CanvasPoint())
        }
    }

    private var pointsTo = arrayListOf<CanvasPoint>().apply {
        repeat(chartLayoutParams.yMarks + 1) {
            add(CanvasPoint())
        }
    }

    private val historyRange = HistoryRange()

    private var animFloat = 0f

    private val rect = Rect()

    private val animator = object : ValueAnimatorWrapper({ value ->
        onAnimateValues(value)
        invalidate()
    }) {
        override fun start() {
            for (i in 0 until pointsTo.size) {
                pointsFrom[i].value = pointsTo[i].value
                pointsFrom[i].canvasValue = pointsTo[i].canvasValue
            }

            historyRange.minStart = chartsData.valueMin
            historyRange.maxStart = chartsData.valueMax
            adjustValuesRange()
            super.start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onAnimateValues(0f)
    }

    override fun onDraw(canvas: Canvas) {
        drawGrid(canvas)
        drawMarks(canvas)
    }

    override fun updateTheme(colors: ChartColors) {
        paints = ChartPaints(context, ChartColors(context))
    }

    fun onAnimateValues(v: Float) { // v: 1 -> 0
        if (height == 0 || width == 0) return
        animFloat = v
        val kY = 1f * (height - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / (historyRange.endInterval - historyRange.deltaInterval * v)
        val mh = height * 1f - chartLayoutParams.paddingBottom
        var min = historyRange.minEnd + v * historyRange.deltaMin
        // scale new points
        pointsTo.forEach { point -> point.canvasValue = mh - kY * (point.value - min) }
        // rescale old points
        min = historyRange.minEnd + (1f - v) * historyRange.deltaMin
        pointsFrom.forEach { point -> point.canvasValue = mh - kY * (point.value - min) }

        invalidate()
    }

    fun adjustValuesRange() {
        val columns = chartsData.columns.values
        columns.forEach { it.calculateBorders(chartsData.timeIndexStart, chartsData.timeIndexEnd) }
        val enabled = columns.filter { it.enabled }
        val chartsMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
        val chartsMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100

        val marks = valueFormatter.marksFromRange(chartsMin, chartsMax, chartLayoutParams.yMarks)
        for (i in 0 until marks.size) {
            pointsTo[i].value = marks[i]
        }

        chartsData.valueMin = pointsTo[0].value
        chartsData.valueMax = pointsTo.last().value

        historyRange.minEnd = chartsData.valueMin
        historyRange.maxEnd = chartsData.valueMax
    }

    fun anim() {
        animator.start()
    }

    fun onTimeIntervalChanged() {
        onAnimateValues(0f)
    }

    private fun drawMarks(canvas: Canvas) {
        val x = chartLayoutParams.paddingHorizontal * 1f

        paints.paintChartText.alpha = ((1f - animFloat) * 255).toInt()
        paints.paintMarksBackground.alpha = paints.paintChartText.alpha
        for (i in 0 until pointsTo.size) {
            val text = valueFormatter.format(pointsTo[i].value)
            paints.paintChartText.getTextBounds(text, 0, text.length, rect)
            val y = pointsTo[i].canvasValue - chartLayoutParams.paddingTextBottom
            canvas.drawText(text, x, y, paints.paintChartText)
        }

        paints.paintChartText.alpha = (animFloat * 255).toInt()
        for (i in 0 until pointsFrom.size) {
            canvas.drawText(valueFormatter.format(pointsFrom[i].value), x, pointsFrom[i].canvasValue - chartLayoutParams.paddingTextBottom, paints.paintChartText)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        paints.paintGrid.alpha = ((1f - animFloat) * 25).toInt()
        val p = chartLayoutParams.paddingHorizontal * 1f
        for (i in 0 until pointsTo.size) {
            canvas.drawLine(p, pointsTo[i].canvasValue, width - p, pointsTo[i].canvasValue, paints.paintGrid)
        }

        paints.paintGrid.alpha = (animFloat * 25).toInt()
        for (i in 0 until pointsFrom.size) {
            canvas.drawLine(p, pointsFrom[i].canvasValue, width - p, pointsFrom[i].canvasValue, paints.paintGrid)
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

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}

private class HistoryRange {
    var minStart = 0
    var minEnd = 0
    var maxStart = 0
    var maxEnd = 0
    val startInterval get() = maxStart - minStart
    val endInterval get() = maxEnd - minEnd
    val deltaInterval get() = endInterval - startInterval
    val deltaMin get() = minEnd - minStart
}

private class CanvasPoint(
    var value: Int = 0,
    var canvasValue: Float = 0f
)

private class PaddingRect(var padding: Int = 0) {
    private var rF = RectF()

    fun rect(r: Rect, dx: Int, dy: Int): RectF {
        rF.left = (r.left - padding + dx).toFloat()
        rF.right = (r.right + padding + dx).toFloat()
        rF.top = (r.top - padding + dy).toFloat()
        rF.bottom = (r.bottom + padding + dy).toFloat()
        return rF
    }
}