package au.sjowl.lib.view.charts.telegram.chart.axis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.SLog
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartConfig
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class AxisY(val v: View) {

    val intervals = ChartConfig.yIntervals

    open var chartsData: ChartsData = ChartsData()

    val chartLayoutParams = ChartLayoutParams(v.context)

    val height get() = v.height

    open val drawGrid = true

    open val windowMin get() = chartsData.windowMin

    open val windowMax get() = chartsData.windowMax

    open val alphaOldPoints get() = ((animScale) * 255).toInt()

    open val alphaNewPoints get() = ((1 - animScale) * 255).toInt()

    protected var paints = AxisPaints(v.context)

    protected var pointsFrom = Points(intervals)

    protected var pointsTo = Points(intervals)

    protected val valueFormatter = ValueFormatter()

    protected var animScale = 0f

    protected var animScroll = 0f

    protected open var textOffset = chartLayoutParams.paddingHorizontal * 1f

    protected var mh = 0f

    protected var isScrolling = false

    protected var isScaling = false

    private var kY = 0f

    private var lastWindowMin = 0

    private var lastWindowMax = 0

    fun onAnimationScrollStart() {
        setVals()
        isScrolling = true
        isScaling = false
        lastWindowMin = windowMin
        lastWindowMax = windowMax
        for (i in 0..intervals) {
            pointsTo.canvasFrom[i] = pointsTo.canvasTo[i]
            pointsTo.canvasTo[i] = canvasY(pointsTo.valuesTo[i])
        }
    }

    fun isIntervalChanged(): Boolean {
        return !(windowMin == lastWindowMin && windowMax == lastWindowMax)
    }

    fun onAnimateScroll(value: Float) {
        animScroll = value
        pointsTo.calcCurrent(animScroll, v.width, chartLayoutParams.paddingHorizontal, chartLayoutParams.paddingHorizontal)
    }

    /**
     * remember current all points
     */
    open fun onAnimationScaleStart() {
        setVals()
        lastWindowMin = windowMin
        lastWindowMax = windowMax
        isScaling = true
        isScrolling = false

        for (i in 0..intervals) {
            pointsFrom.valuesFrom[i] = pointsTo.valuesTo[i]
            pointsFrom.valuesTo[i] = pointsTo.valuesTo[i]
            pointsFrom.canvasFrom[i] = pointsTo.canvasTo[i]
            pointsFrom.canvasTo[i] = canvasY(pointsTo.valuesTo[i])
        }

        pointsTo.valuesTo = valueFormatter.rawMarksFromRange(windowMin, windowMax, intervals).toIntArray()
        for (i in 0..intervals) {
            pointsTo.canvasTo[i] = canvasY(pointsTo.valuesTo[i])
            pointsTo.canvasFrom[i] = pointsTo.canvasTo[i]
            pointsTo.valuesFrom[i] = pointsTo.valuesTo[i]
        }
    }

    open fun ky(): Float = (mh - chartLayoutParams.paddingTop) / chartsData.windowValueInterval

    fun canvasY(value: Int) = mh - kY * (value - windowMin)

    open fun onAnimateScale(value: Float) {
        animScale = value
        // update alpha
        pointsTo.calcCurrent(animScale, v.width, chartLayoutParams.paddingHorizontal, chartLayoutParams.paddingHorizontal)
        pointsFrom.calcCurrent(animScale, v.width, chartLayoutParams.paddingHorizontal, chartLayoutParams.paddingHorizontal)
    }

    open fun drawMarks(canvas: Canvas) {
        val x = textOffset
        if (isScaling) {
            // old
            paints.paintChartText.alpha = alphaOldPoints
            drawTitlesFrom(canvas)
            // new
            paints.paintChartText.alpha = alphaNewPoints
            drawTitlesTo(canvas)
        }
        if (isScrolling) {
            paints.paintChartText.alpha = 255
            drawTitlesTo(canvas)
        }
    }

    fun drawTitlesFrom(canvas: Canvas) {
        val x = textOffset
        for (i in 0..intervals) {
            val y = pointsFrom.currentCanvas[i] - chartLayoutParams.paddingTextBottom
            canvas.drawText(pointsFrom.valuesFrom[i].toString(), x, y, paints.paintChartText)
        }
    }

    fun drawTitlesTo(canvas: Canvas) {
        val x = textOffset
        for (i in 0..intervals) {
            val y = pointsTo.currentCanvas[i] - chartLayoutParams.paddingTextBottom
            canvas.drawText(pointsTo.valuesTo[i].toString(), x, y, paints.paintChartText)
        }
    }

    open fun drawGrid(canvas: Canvas) {
        if (!drawGrid) return
        if (isScaling) {
            paints.paintGrid.alpha = ((animScale) * 25).toInt()
            canvas.drawLines(pointsFrom.gridPoints, paints.paintGrid)

            paints.paintGrid.alpha = ((1f - animScale) * 25).toInt()
            canvas.drawLines(pointsTo.gridPoints, paints.paintGrid)
        }
        if (isScrolling) {
            paints.paintGrid.alpha = 25
            canvas.drawLines(pointsTo.gridPoints, paints.paintGrid)
        }
    }

    open fun updateTheme(context: Context) {
        this.paints = AxisPaints(v.context)
    }

    fun draw(canvas: Canvas) {
        drawGrid(canvas)
        drawMarks(canvas)
    }

    private fun setVals() {
        mh = v.height * 1f - chartLayoutParams.paddingBottom
        kY = ky()
    }

    inner class Points(cap: Int) {

        val capacity = cap + 1

        var canvasFrom = FloatArray(capacity)
        var canvasTo = FloatArray(capacity)
        var valuesFrom = IntArray(capacity)
        var valuesTo = IntArray(capacity)
        var gridPoints = FloatArray(capacity * 4)
        val currentCanvas = FloatArray(capacity)

        fun calcCurrent(v: Float, width: Int, paddingLeft: Int, paddingRight: Int) {
            for (i in 0 until capacity) {
                currentCanvas[i] = canvasTo[i] - (canvasTo[i] - canvasFrom[i]) * v
                val j = i * 4
                gridPoints[j] = paddingLeft * 1f
                gridPoints[j + 1] = currentCanvas[i]
                gridPoints[j + 2] = width - paddingRight * 1f
                gridPoints[j + 3] = currentCanvas[i]
            }
        }

        fun print(msg: String) {
            SLog.d("******************* $msg")
            SLog.d("canvasFrom = ${floatArrayS(canvasFrom)}")
            SLog.d("canvasTo = ${floatArrayS(canvasTo)}")
            SLog.d("valuesFrom = ${intArrayS(valuesFrom)}")
            SLog.d("valuesTo = ${intArrayS(valuesTo)}")
            SLog.d("gridPoints = ${floatArrayS(gridPoints)}")
        }
    }

    class AxisPaints(context: Context) : BasePaints(context) {

        val paintGrid = Paint().apply {
            color = colors.gridLines
            style = Paint.Style.STROKE
            strokeWidth = dimensions.gridWidth
            strokeCap = Paint.Cap.ROUND
        }

        val paintChartText = paint().apply {
            color = colors.chartText
            textSize = dimensions.axisTextHeight
        }
    }
}

private fun floatArrayS(a: FloatArray): String {
    var s = "["
    for (i in 0 until a.size) {
        s += "${a[i]}, "
    }
    s += "]"
    return s
}

private fun intArrayS(a: IntArray): String {
    var s = "["
    for (i in 0 until a.size) {
        s += "${a[i]}, "
    }
    s += "]"
    return s
}