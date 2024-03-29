package au.sjowl.lib.view.charts.telegram.overview.linear

import android.graphics.Canvas
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.SLog
import au.sjowl.lib.view.charts.telegram.overview.scroll.ChartOverviewScrollView
import au.sjowl.lib.view.charts.telegram.overview.scroll.OverviewScrollLayoutParams

open class OverviewChart(
    val chartData: ChartData,
    val layoutHelper: OverviewScrollLayoutParams,
    var paints: ChartOverviewScrollView.ChartOverviewPaints,
    val chartsData: ChartsData
) {

    var min = 0

    var max = 0

    protected var enabled = chartData.enabled

    protected var alpha = 1f

    private var points = FloatArray(chartData.values.size shl 1)

    private var pointsFrom = FloatArray(chartData.values.size shl 1)

    private var drawingPoints = FloatArray(chartData.values.size shl 1)

    private val pointsPerDip = 30f

    private var numberOfPointsToDraw: Int = (layoutHelper.w0 / layoutHelper.dip * pointsPerDip).toInt()

    private var xmin = 0L

    private var h = 0f

    private var mh = 0f

    private var kX = 0f

    private var kY = 0f

    private var animValue = 0f

    private var truncatedSize = 0

    fun setupPoints() {
        if (layoutHelper.w0 <= 0) {
            // todo error should not be
            SLog.d("error: setup points for w < 0")
            return
        }
        numberOfPointsToDraw = (layoutHelper.w0 / layoutHelper.dip * pointsPerDip).toInt()
        points = FloatArray((numberOfPointsToDraw shl 1).toInt())
        pointsFrom = FloatArray((numberOfPointsToDraw shl 1).toInt())
        drawingPoints = FloatArray((numberOfPointsToDraw shl 1).toInt())

        setVals()
        val t = chartsData.times
        val column = chartData.values
        var j = 0
        val step = step()
        for (i in 0 until t.size - step step step) {
            points[j++] = kX * (t[i] - xmin) + layoutHelper.paddingHorizontal0
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
            Math.ceil((1f * chartsData.times.size / numberOfPointsToDraw).toDouble()).toInt())
    }

    private inline fun setVals() {
        if (chartsData.isYScaled) {
            min = chartData.chartMin
            max = chartData.chartMax
        }
        xmin = chartsData.time.min
        h = 1f * (layoutHelper.h0 - 2 * layoutHelper.paddingVertical)
        mh = layoutHelper.h0 * 1f
        kX = layoutHelper.w0 * 1f / chartsData.time.interval
        kY = h / (max - min)
    }
}