package au.sjowl.lib.view.charts.telegram.overview

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartPaints

class OverviewChart(
    val data: ChartData,
    val layoutHelper: OverviewLayoutParams,
    var paints: ChartPaints,
    val chartsData: ChartsData
) {

    var min = 0

    var max = 0

    protected var enabled = data.enabled

    protected var alpha = 1f

    private var points = FloatArray(data.values.size * 2)

    private var pointsFrom = FloatArray(data.values.size * 2)

    private var drawingPoints = FloatArray(data.values.size * 2)

    private val pointsPerDip = 30f

    private var numberOfPointsToDraw = layoutHelper.w / layoutHelper.dip * pointsPerDip

    private var xmin = 0L

    private var h = 0f

    private var mh = 0f

    private var kX = 0f

    private var kY = 0f

    private var animValue = 0f

    private var truncatedSize = 0

    fun setupPoints() {
        numberOfPointsToDraw = layoutHelper.w / layoutHelper.dip * pointsPerDip
        points = FloatArray((numberOfPointsToDraw * 2).toInt())
        pointsFrom = FloatArray((numberOfPointsToDraw * 2).toInt())
        drawingPoints = FloatArray((numberOfPointsToDraw * 2).toInt())

        setVals()
        val t = chartsData.time.values
        val column = data.values
        var j = 0
        val step = step()
        for (i in 0 until t.size - step step step) {
            points[j++] = kX * (t[i] - xmin) + layoutHelper.paddingHorizontal
            points[j++] = mh - kY * (column[i] - min)
        }
        truncatedSize = j
        points.copyInto(drawingPoints)
    }

    fun updateStartPoints() {
        drawingPoints.copyInto(pointsFrom)
        enabled = data.enabled
    }

    fun updateFinishState() {
        setVals()
        val column = data.values
        var j = 1
        val step = step()
        for (i in 0 until column.size - step step step) {
            points[j] = mh - kY * (column[i] - min)
            j += 2
        }
    }

    fun onAnimateValues(v: Float) {
        alpha = when {
            data.enabled && enabled -> 1f
            data.enabled && !enabled -> 1f - v
            !data.enabled && !enabled -> 0f
            else -> v
        }
        animValue = v
        for (i in 0 until (points.size - 1) step 2) {
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
        }
    }

    fun draw(canvas: Canvas) {
        if (data.enabled || enabled) {
            val paint = paints.paintOverviewLine
            paint.color = data.color
            paint.alpha = (alpha * 255).toInt()
            canvas.drawLines(drawingPoints, 0, truncatedSize, paint)
            canvas.drawLines(drawingPoints, 2, truncatedSize - 2, paint)
        }
    }

    private fun step(): Int {
        return Math.ceil((1f * chartsData.time.values.size / numberOfPointsToDraw).toDouble()).toInt()
    }

    private inline fun setVals() {
        xmin = chartsData.time.min
        h = 1f * (layoutHelper.h - 2 * layoutHelper.paddingVertical)
        mh = layoutHelper.h * 1f
        kX = layoutHelper.w * 1f / chartsData.time.interval
        kY = h / (max - min)
    }
}