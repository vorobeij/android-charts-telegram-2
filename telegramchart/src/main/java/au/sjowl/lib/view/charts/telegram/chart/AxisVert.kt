package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.CanvasPoint
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams
import org.jetbrains.anko.sp

open class AxisVert(
    yMarks: Int,
    val context: Context,
    protected val chartLayoutParams: ChartLayoutParams
) {

    val min get() = pointsTo[0].value

    val max get() = pointsTo.last().value

    protected var paints = AxisPaints(context)

    protected var pointsFrom = points(yMarks)

    protected var pointsTo = points(yMarks)

    protected val rect = Rect()

    protected val valueFormatter = ValueFormatter()

    protected var animFloat = 0f

    protected open var textOffset = chartLayoutParams.paddingHorizontal * 1f

    private val historyRange = HistoryRange()

    fun calculateMarks(chartMin: Int, chartMax: Int) {
        val marks = valueFormatter.marksFromRange(chartMin, chartMax, chartLayoutParams.yMarks)
        for (i in 0 until marks.size) {
            pointsTo[i].value = marks[i]
        }
        historyRange.minEnd = min
        historyRange.maxEnd = max
    }

    open fun beforeAnimate(chartsData: ChartsData) {
        for (i in 0 until pointsTo.size) {
            pointsFrom[i].value = pointsTo[i].value
            pointsFrom[i].canvasValue = pointsTo[i].canvasValue
        }

        historyRange.minStart = chartsData.valueMin
        historyRange.maxStart = chartsData.valueMax
    }

    open fun onAnimate(v: Float, height: Int) {
        animFloat = v
        calculatePoints(height)
    }

    open fun calculatePoints(height: Int) {
        val kY = 1f * (height - chartLayoutParams.paddingBottom - chartLayoutParams.paddingTop) / (historyRange.endInterval - historyRange.deltaInterval * animFloat)
        val mh = height * 1f - chartLayoutParams.paddingBottom
        var min = historyRange.minEnd + animFloat * historyRange.deltaMin
        // scale new points
        pointsTo.forEach { point -> point.canvasValue = mh - kY * (point.value - min) }
        // rescale old points
        min = historyRange.minEnd + (1f - animFloat) * historyRange.deltaMin
        pointsFrom.forEach { point -> point.canvasValue = mh - kY * (point.value - min) }
    }

    open fun initDraw() {
    }

    open fun drawMarks(canvas: Canvas) {
        initDraw()
        val x = textOffset
        paints.paintChartText.alpha = ((1f - animFloat) * 255).toInt()
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

    open fun drawGrid(canvas: Canvas, width: Int, height: Int) {
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

    open fun updateTheme() {
        this.paints = AxisPaints(context)
    }

    private fun points(yMarks: Int) = arrayListOf<CanvasPoint>().apply {
        repeat(yMarks + 1) {
            add(CanvasPoint())
        }
    }

    class AxisPaints(context: Context) : BasePaints(context) {

        val paintGrid = paint().apply {
            color = colors.gridLines
            style = Paint.Style.STROKE
            strokeWidth = 3f
            strokeCap = Paint.Cap.ROUND
        }

        val paintChartText = paint().apply {
            color = colors.chartText
            textSize = context.sp(12f) * 1f
        }
    }
}

class AxisLeft(
    yMarks: Int,
    context: Context,
    chartLayoutParams: ChartLayoutParams,
    val chart: ChartData
) : AxisVert(yMarks, context, chartLayoutParams) {
    override fun initDraw() {
        textOffset = chartLayoutParams.paddingHorizontal * 1f
        paints.paintChartText.color = chart.color
    }
}

class AxisRight(
    yMarks: Int,
    context: Context,
    chartLayoutParams: ChartLayoutParams,
    val chart: ChartData
) : AxisVert(yMarks, context, chartLayoutParams) {
    override fun initDraw() {
        textOffset = chartLayoutParams.w - chartLayoutParams.paddingHorizontal * 1f - paints.paintChartText.measureText("2000")
        paints.paintChartText.color = chart.color
    }
}