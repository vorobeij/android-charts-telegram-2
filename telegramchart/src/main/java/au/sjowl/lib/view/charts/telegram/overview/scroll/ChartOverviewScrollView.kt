package au.sjowl.lib.view.charts.telegram.overview.scroll

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartDimensions

class ChartOverviewScrollView : View, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            update()
            invalidate()
        }

    var onTimeIntervalChanged: (() -> Unit) = {}

    private val touchHelper = TouchHelper()

    private val layoutHelper = OverviewScrollLayoutParams(context)

    private val dimensions = ChartDimensions(context)

    private val rectangles = OverviewRectangles(dimensions.overviewTouchWidth)

    private var paints = ChartOverviewPaints(context)

    private val pathClipBorder = Path()

    private val pathClipWindow = Path()

    private val knobPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        update()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(pathClipBorder)

        drawTint(canvas)

        canvas.restore()

        drawWindow(canvas)
    }

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
                            e >= chartsData.times.size -> {
                                chartsData.timeIndexEnd = chartsData.times.size - 1
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
                        chartsData.timeIndexEnd = if (chartsData.timeIndexEnd >= chartsData.times.size) chartsData.times.size - 1 else chartsData.timeIndexEnd
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

    override fun updateTheme() {
        paints = ChartOverviewPaints(context)
        invalidate()
    }

    private fun update() {
        layoutHelper.h0 = measuredHeight.toFloat()
        layoutHelper.w0 = measuredWidth.toFloat()
        val pv = layoutHelper.paddingVertical * 1f
        rectangles.reset(0f, pv, measuredWidth * 1f, measuredHeight - pv, layoutHelper.windowOffset, 0)

        pathClipBorder.reset()
        pathClipBorder.addRoundRect(rectangles.border, layoutHelper.radiusBorder, layoutHelper.radiusBorder, Path.Direction.CW)
    }

    private fun drawWindow(canvas: Canvas) {

        rectangles.setTimeWindow(
            timeToCanvas(chartsData.timeIndexStart),
            timeToCanvas(chartsData.timeIndexEnd),
            layoutHelper.windowBorder / 2f
        )

        with(canvas) {
            pathClipWindow.reset()
            pathClipWindow.addRoundRect(rectangles.timeWindowClip, layoutHelper.radiusWindow, layoutHelper.radiusWindow, Path.Direction.CW)

            save()
            clipPath(pathClipWindow)

            // verticals
            drawVerticalWindowBorder(canvas, timeToCanvas(chartsData.timeIndexStart))
            drawVerticalWindowBorder(canvas, timeToCanvas(chartsData.timeIndexEnd))

            // horizontals
            val dh = paints.paintOverviewWindowHorizontals.strokeWidth / 2
            val dw = rectangles.windowBorderLeft.width() / 2
            val top = layoutHelper.paddingVertical * 1f - dh
            val left = rectangles.timeWindow.left + dw
            val right = rectangles.timeWindow.right - dw
            val bottom = measuredHeight * 1f - layoutHelper.paddingVertical + dh
            drawLine(left, top, right, top, paints.paintOverviewWindowHorizontals)
            drawLine(left, bottom, right, bottom, paints.paintOverviewWindowHorizontals)

            // knobs
            drawKnob(canvas, rectangles.timeWindow.left)
            drawKnob(canvas, rectangles.timeWindow.right)

            restore()
        }
    }

    private fun drawKnob(canvas: Canvas, c: Float) {
        val dy = (measuredHeight - layoutHelper.knobHeight) / 2
        knobPath.reset()
        knobPath.moveTo(c, dy)
        knobPath.lineTo(c, height - dy)
        canvas.drawPath(knobPath, paints.paintOverviewWindowKnob)
    }

    private fun drawVerticalWindowBorder(canvas: Canvas, x: Float) {
        canvas.drawLine(x, 0f, x, height * 1f, paints.paintOverviewWindowVerticals)
    }

    private fun drawTint(canvas: Canvas) {
        rectangles.bgLeft.right = timeToCanvas(chartsData.timeIndexStart) + dimensions.overviewWindowBorder / 2
        rectangles.bgRight.left = timeToCanvas(chartsData.timeIndexEnd) - dimensions.overviewWindowBorder / 2
        canvas.drawRect(rectangles.bgLeft, paints.paintOverviewWindowTint)
        canvas.drawRect(rectangles.bgRight, paints.paintOverviewWindowTint)
    }

    private inline fun timeToCanvas(timeIndex: Int): Float = layoutHelper.paddingHorizontal0 + (layoutHelper.w0) * 1f * timeIndex / (chartsData.times.size - 1)

    private inline fun canvasToIndexInterval(canvasDistance: Int): Int = (canvasDistance * chartsData.times.size / layoutHelper.w0).toInt()

    companion object {
        const val TOUCH_NONE = -1
        const val TOUCH_DRAG = 0
        const val TOUCH_SCALE_LEFT = 1
        const val TOUCH_SCALE_RIGHT = 2
        const val SCALE_THRESHOLD = 14
    }

    class ChartOverviewPaints(context: Context) : BasePaints(context) {
        val paintOverviewWindowVerticals = simplePaint().apply {
            style = Paint.Style.STROKE
            strokeWidth = dimensions.overviewWindowBorder
            color = colors.scrollSelector
        }

        val paintOverviewWindowKnob = simplePaint().apply {
            style = Paint.Style.STROKE
            color = colors.colorKnob
            strokeWidth = dimensions.overviewKnobWidth
            strokeCap = Paint.Cap.ROUND
        }

        val paintOverviewWindowHorizontals = simplePaint().apply {
            strokeWidth = 5f
            style = Paint.Style.STROKE
            color = colors.scrollSelector
        }

        val paintOverviewWindowTint = simplePaint().apply {
            style = Paint.Style.FILL
            color = colors.scrollBackground
        }

        val paintOverviewLine = simplePaint().apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}