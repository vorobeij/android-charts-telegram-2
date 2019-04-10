package au.sjowl.lib.view.charts.telegram.overview

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

open class OverviewChart(
    val chartData: ChartData,
    val layoutHelper: OverviewLayoutParams,
    var paints: ChartOverviewView.ChartOverviewPaints,
    val chartsData: ChartsData
) {

    var min = 0

    var max = 0

    protected var enabled = chartData.enabled

    protected var alpha = 1f

    private var points = FloatArray(chartData.values.size * 2)

    private var pointsFrom = FloatArray(chartData.values.size * 2)

    private var drawingPoints = FloatArray(chartData.values.size * 2)

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
        if (layoutHelper.w <= 0) {
            // todo error should not be
            println("error: setup points for w < 0")
            return
        }
        numberOfPointsToDraw = layoutHelper.w / layoutHelper.dip * pointsPerDip
        points = FloatArray((numberOfPointsToDraw * 2).toInt())
        pointsFrom = FloatArray((numberOfPointsToDraw * 2).toInt())
        drawingPoints = FloatArray((numberOfPointsToDraw * 2).toInt())

        setVals()
        val t = chartsData.time.values
        val column = chartData.values
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
        enabled = chartData.enabled
    }

    fun updateFinishState() {
        setVals()
        val column = chartData.values
        var j = 1
        val step = step()
        for (i in 0 until column.size - step step step) {
            points[j] = mh - kY * (column[i] - min)
            j += 2
        }
    }

    fun onAnimateValues(v: Float) {
        alpha = when {
            chartData.enabled && enabled -> 1f
            chartData.enabled && !enabled -> 1f - v
            !chartData.enabled && !enabled -> 0f
            else -> v
        }
        animValue = v
        for (i in 0 until (points.size - 1) step 2) {
            drawingPoints[i + 1] = points[i + 1] + (pointsFrom[i + 1] - points[i + 1]) * animValue
        }
    }

    fun draw(canvas: Canvas) {
        if (chartData.enabled || enabled) {
            val paint = paints.paintOverviewLine
            paint.color = chartData.color
            paint.alpha = (alpha * 255).toInt()
            canvas.drawLines(drawingPoints, 0, truncatedSize, paint)
            canvas.drawLines(drawingPoints, 2, truncatedSize - 2, paint)
        }
    }

    private fun step(): Int {
        return Math.max(1,
            Math.ceil((1f * chartsData.time.values.size / numberOfPointsToDraw).toDouble()).toInt())
    }

    private inline fun setVals() {
        if (chartsData.isYScaled) {
            min = chartData.chartMin
            max = chartData.chartMax
        }
        xmin = chartsData.time.min
        h = 1f * (layoutHelper.h - 2 * layoutHelper.paddingVertical)
        mh = layoutHelper.h * 1f
        kX = layoutHelper.w * 1f / chartsData.time.interval
        kY = h / (max - min)
    }
}