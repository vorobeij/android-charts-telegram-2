package au.sjowl.lib.view.charts.telegram.overview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import au.sjowl.lib.view.charts.telegram.BaseSurfaceView
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartPaints
import org.jetbrains.anko.dip

class ChartOverviewView : BaseSurfaceView, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            value.columns.values.forEach { chartColumn ->
                chartColumn.calculateBorders()
                value.columns.values.forEach { charts.add(OverviewChart(it, layoutHelper, paints, value)) }
                setChartsRange()
            }
        }

    var onTimeIntervalChanged: (() -> Unit) = {}

    private val touchHelper = TouchHelper()

    private val layoutHelper = OverviewLayoutParams(context)

    private val rectangles = OverviewRectangles(context.dip(10))

    private val charts = arrayListOf<OverviewChart>()

    private var paints = ChartPaints(context, ChartColors(context))

    private var chartsBmp: Bitmap? = null

    private val chartsCanvas = Canvas()

    private val pathClipBorder = Path()
    private val pathClipWindow = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        layoutHelper.h = measuredHeight.toFloat()
        layoutHelper.w = measuredWidth.toFloat()
        val ph = layoutHelper.paddingHorizontal * 1f
        val pv = layoutHelper.paddingVertical * 1f
        rectangles.reset(ph, pv, layoutHelper.w + ph, measuredHeight - pv, layoutHelper.windowOffset, ph.toInt())

        pathClipBorder.reset()
        pathClipBorder.addRoundRect(rectangles.border, layoutHelper.radiusBorder, layoutHelper.radiusBorder, Path.Direction.CW)

        charts.forEach { it.setupPoints() }
        createChartsBitmap()
        invalidateChartsBitmap()
    }

    // todo scale with 2 pointers
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectangles.updateTouch()
                touchHelper.touchMode = rectangles.getTouchMode(event.x, event.y)

                touchHelper.xDown = event.x
                touchHelper.timeStartDownIndex = chartsData.timeIndexStart
                touchHelper.timeEndDownIndex = chartsData.timeIndexEnd
            }
            MotionEvent.ACTION_MOVE -> {
                val delta = touchHelper.xDown - event.x
                val deltaIndex = -canvasToIndexInterval(delta.toInt())
                when (touchHelper.touchMode) {
                    TOUCH_NONE -> {
                    }
                    TOUCH_DRAG -> {
                        val w = chartsData.timeIndexEnd - chartsData.timeIndexStart
                        val s = touchHelper.timeStartDownIndex + deltaIndex
                        val e = touchHelper.timeEndDownIndex + deltaIndex

                        when {
                            s < 0 -> {
                                chartsData.timeIndexStart = 0
                                chartsData.timeIndexEnd = chartsData.timeIndexStart + w
                            }
                            e >= chartsData.time.values.size -> {
                                chartsData.timeIndexEnd = chartsData.time.values.size - 1
                                chartsData.timeIndexStart = chartsData.timeIndexEnd - w
                            }
                            else -> {
                                chartsData.timeIndexEnd = e
                                chartsData.timeIndexStart = s
                            }
                        }
                        chartsData.scaleInProgress = true
                        onTimeIntervalChanged()
                        invalidate()
                    }
                    TOUCH_SCALE_LEFT -> {
                        chartsData.timeIndexStart = Math.min(touchHelper.timeStartDownIndex + deltaIndex, chartsData.timeIndexEnd - SCALE_THRESHOLD)
                        chartsData.timeIndexStart = if (chartsData.timeIndexStart < 0) 0 else chartsData.timeIndexStart
                        chartsData.scaleInProgress = true
                        onTimeIntervalChanged()
                        invalidate()
                    }
                    TOUCH_SCALE_RIGHT -> {
                        chartsData.timeIndexEnd = Math.max(touchHelper.timeEndDownIndex + deltaIndex, chartsData.timeIndexStart + SCALE_THRESHOLD)
                        chartsData.timeIndexEnd = if (chartsData.timeIndexEnd >= chartsData.time.values.size) chartsData.time.values.size - 1 else chartsData.timeIndexEnd
                        chartsData.scaleInProgress = true
                        onTimeIntervalChanged()
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                if (touchHelper.touchMode != TOUCH_NONE) {
                    touchHelper.touchMode = TOUCH_NONE
                    chartsData.scaleInProgress = false
                    onTimeIntervalChanged()
                }
            }
        }
        return true
    }

    override fun drawSurface(canvas: Canvas) {
        canvas.drawColor(paints.colors.background)

        canvas.save()
        canvas.clipPath(pathClipBorder)

        drawCharts(canvas)
        drawBackground(canvas)

        canvas.restore()

        drawWindow(canvas)
    }

    override fun updateTheme(colors: ChartColors) {
        paints = ChartPaints(context, colors)
        invalidate()
    }

    fun updateStartPoints() {
        charts.forEach { it.updateStartPoints() }
    }

    fun onAnimateValues(v: Float) {
        charts.forEach { it.onAnimateValues(v) }
        invalidate()
    }

    fun updateFinishState() {
        setChartsRange()
        charts.forEach { it.updateFinishState() }
    }

    private fun drawCharts(canvas: Canvas) {
        charts.forEach { it.draw(canvas) }
    }

    private fun createChartsBitmap() {
        if (measuredHeight > 0 && measuredWidth > 0) {
            chartsBmp?.recycle()
            chartsBmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            chartsCanvas.setBitmap(chartsBmp)
        }
    }

    private fun invalidateChartsBitmap() {
        if (measuredHeight > 0 && measuredWidth > 0) {
            charts.forEach { it.draw(chartsCanvas) }
        }
    }

    private fun setChartsRange() {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        chartsData.columns.values.filter { it.enabled }.forEach { chart ->
            if (chart.min < min) min = chart.chartMin
            if (chart.max > max) max = chart.chartMax
        }

        charts.forEach {
            it.min = min
            it.max = max
        }
    }

    private fun drawWindow(canvas: Canvas) {

        rectangles.setTimeWindow(
            timeToCanvas(chartsData.timeIndexStart),
            timeToCanvas(chartsData.timeIndexEnd),
            layoutHelper.windowBorder / 2
        )

        with(canvas) {
            pathClipWindow.reset()
            pathClipWindow.addRoundRect(rectangles.timeWindowClip, layoutHelper.radiusWindow, layoutHelper.radiusWindow, Path.Direction.CW)

            save()
            clipPath(pathClipWindow)

            drawRect(rectangles.windowBorderLeft, paints.paintOverviewWindowVerticals)
            drawRect(rectangles.windowBorderRight, paints.paintOverviewWindowVerticals)

            val dw = rectangles.windowBorderLeft.width() / 2
            val top = layoutHelper.paddingVertical * 1f
            val left = rectangles.timeWindow.left + dw
            val right = rectangles.timeWindow.right - dw
            val bottom = measuredHeight * 1f - top
            drawLine(left, top, right, top, paints.paintOverviewWindowHorizontals)
            drawLine(left, bottom, right, bottom, paints.paintOverviewWindowHorizontals)

            val dy = (measuredHeight - layoutHelper.knobHeight) / 2
            val w = layoutHelper.knobWidth
            var c = rectangles.timeWindow.left
            drawRoundRect(c - w, dy, c + w, height - dy, w, w, paints.paintOverviewWindowKnob)
            c = rectangles.timeWindow.right
            drawRoundRect(c - w, dy, c + w, height - dy, w, w, paints.paintOverviewWindowKnob)

            restore()
        }
    }

    private fun drawBackground(canvas: Canvas) {
        rectangles.bgLeft.right = timeToCanvas(chartsData.timeIndexStart)
        rectangles.bgRight.left = timeToCanvas(chartsData.timeIndexEnd)
        canvas.drawRect(rectangles.bgLeft, paints.paintOverviewWindowTint)
        canvas.drawRect(rectangles.bgRight, paints.paintOverviewWindowTint)
    }

    private inline fun timeToCanvas(timeIndex: Int): Float = layoutHelper.paddingHorizontal + (layoutHelper.w) * 1f * timeIndex / chartsData.time.values.size

    private inline fun canvasToIndexInterval(canvasDistance: Int): Int = (canvasDistance * chartsData.time.values.size / layoutHelper.w).toInt()

    companion object {
        const val TOUCH_NONE = -1
        const val TOUCH_DRAG = 0
        const val TOUCH_SCALE_LEFT = 1
        const val TOUCH_SCALE_RIGHT = 2
        const val SCALE_THRESHOLD = 14
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}