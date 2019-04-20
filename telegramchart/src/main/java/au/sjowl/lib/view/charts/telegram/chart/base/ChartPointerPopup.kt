package au.sjowl.lib.view.charts.telegram.chart.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ChartAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.drawCompatRoundRect
import au.sjowl.lib.view.charts.telegram.other.getTextBounds
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartConfig
import au.sjowl.lib.view.charts.telegram.time.TimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale

open class ChartPointerPopup : View, ThemedView {

    var paints = ChartPointerPaints(context)

    var chartsData = ChartsData()
        set(value) {
            field = value
            timeFormatter = if (value.isZoomed) HourFormatter() else DayFormatter()
            items = chartsData.charts.map { ChartPoint(it) }
        }

    var h0: Int = 0

    protected var title = ""

    protected open var items = listOf<ChartPoint>()

    protected var rectRadius = paints.dimensions.pointerRadius

    protected val timeIndex get() = chartsData.pointerTimeIndex

    protected var w = 0f

    protected var h = 0f

    protected var leftBorder = 0f

    protected val r1 = Rect()

    protected val r2 = Rect()

    protected var padV = paints.dimensions.pointerPaddingVert

    protected var padH = paints.dimensions.pointerPaddingHorizontal

    protected var mw = 0

    protected val arrowWidth = paints.dimensions.pointerArrowWidth

    protected val arrow = Arrow(paints.dimensions.pointerArrowSize.toInt())

    protected var timeFormatter: TimeFormatter = DayFormatter()

    protected var animValue = 0f

    private var translationAnimation: ObjectAnimator? = null

    private val animator = ChartAnimatorWrapper(
        onStart = {
            for (i in 0 until chartsData.charts.size) {
                items[i].enabledOld = items[i].enabledNew
                items[i].enabledNew = chartsData.charts[i].enabled
            }
        },
        onAnimate = { value ->
            animValue = value
            measure()
            invalidate()
        })

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            setLayerType(LAYER_TYPE_HARDWARE, null)
        } else {
            setLayerType(LAYER_TYPE_SOFTWARE, null) // shadows are not hardware supported on pre lollipop
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measure()
    }

    override fun onDraw(canvas: Canvas) {
        restrictX()

        canvas.drawCompatRoundRect(leftBorder, padV, leftBorder + w, h + padV, rectRadius, rectRadius, paints.paintPointerBackground)

        // draw title
        paints.paintPointerTitle.getTextBounds(title, r1)
        var y = 2 * padV + r1.height()
        canvas.drawText(title, leftBorder + padH, y, paints.paintPointerTitle)

        h0 = itemHeight()
        // draw items
        items.forEach {
            y += it.height
            it.draw(canvas, y)
        }

        if (chartsData.canBeZoomed) {
            arrow.draw(leftBorder + w - padH - arrow.size / 2, 2f * padV, canvas, paints.paintArrow)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isInBounds(event.x, event.y)) {
                    performClick()
                    return true
                }
                return false
            }
        }
        return false
    }

    override fun updateTheme() {
        paints = ChartPointerPaints(context)
        invalidate()
    }

    fun isInBounds(x: Float, y: Float): Boolean {
        return isVisible && x in leftBorder..leftBorder + w && y in 2f * padV..2f * padV + h
    }

    open fun updatePoints() {
        this.mw = measuredWidth

        title = timeFormatter.format(chartsData.times[timeIndex])

        for (i in 0 until chartsData.charts.size) {
            items[i].enabledNew = chartsData.charts[i].enabled
        }
    }

    open fun onChartStateChanged() {
        animator.start()
    }

    protected fun itemHeight(): Int {
        paints.paintPointerValue.textSize = paints.dimensions.pointerValueText
        paints.paintPointerName.textSize = paints.dimensions.pointerNameText
        paints.paintPointerValue.getTextBounds("4050", r1)
        paints.paintPointerName.getTextBounds("Apd", r2)
        return Math.max(r1.height(), r2.height())
    }

    protected fun restrictX() {
        translationAnimation?.cancel()
        translationAnimation = null
        leftBorder = chartsData.pointerTimeX - w * 1.1f - padH - chartsData.barHalfWidth

        if (leftBorder < padH) {
            leftBorder = chartsData.pointerTimeX + padH + chartsData.barHalfWidth
        }
        translationAnimation = ObjectAnimator.ofFloat(this, "translationX", translationX, leftBorder).apply {
            duration = ChartConfig.animDuration
        }
        leftBorder = padH
        translationAnimation?.start()
    }

    protected open fun measure() {
        w = Math.max(
            2f * padH + (items.map { it.width }.max() ?: 0f),
            paints.paintPointerTitle.measureText(title) + arrowWidth
        ) + 2f * padH

        val h0 = itemHeight()
        val valuesHeight = items.map { point -> (h0 + padV) * point.scale(animValue) }.sum()
        h = padV + h0 + valuesHeight + padV + padV / 2

        setMeasuredDimension((w * 1.3f).toInt(), (h * 1.3f).toInt())
    }

    class ChartPointerPaints(context: Context) : BasePaints(context) {
        val paintArrow = simplePaint().apply {
            color = colors.tooltipArrow
            style = Paint.Style.STROKE
            strokeWidth = dimensions.arrowWidth
            strokeCap = Paint.Cap.ROUND
        }

        val paintPointerBackground = simplePaint().apply {
            color = colors.pointer
            setShadowLayer(5f, 0f, 2f, colors.pointerShadow)
        }

        val paintPointerTitle = antiAliasPaint().apply {
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            color = colors.text
            textSize = dimensions.pointerTitle
        }

        val paintPointerValue = antiAliasPaint().apply {
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            textSize = dimensions.pointerValueText
        }

        val paintPointerName = antiAliasPaint().apply {
            textSize = dimensions.pointerNameText
            color = colors.text
        }
    }

    class DayFormatter : TimeFormatter() {
        override val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }

    class HourFormatter : TimeFormatter() {
        override val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    }

    open inner class ChartPoint(
        val chart: ChartData
    ) {

        var enabledOld: Boolean = true
        var enabledNew: Boolean = true

        open val width get() = paints.paintPointerValue.measureText(value) + paints.paintPointerName.measureText(chartName)

        val chartName: String get() = chart.name

        val value get() = chart.values[chartsData.pointerTimeIndex].toString()

        val color: Int get() = chart.color

        val total get() = chartsData.sums[chartsData.pointerTimeIndex]

        val percent: Int get() = if (total == 0) 0 else 100 * chart.values[chartsData.pointerTimeIndex] / total

        val height get() = (h0 + padV) * scale(animValue)

        open fun draw(canvas: Canvas, y: Float) {
            paints.paintPointerValue.color = color
            paints.paintPointerValue.getTextBounds(value, r1)
            paints.paintPointerName.getTextBounds(chartName, r2)

            paints.paintPointerValue.alpha = scale(animValue).toAlpha()
            paints.paintPointerName.alpha = scale(animValue).toAlpha()
            paints.paintPointerValue.textSize = paints.dimensions.pointerValueText * scale(animValue)
            paints.paintPointerName.textSize = paints.dimensions.pointerNameText * scale(animValue)

            canvas.drawText(chartName, leftBorder + padH, y, paints.paintPointerName)
            canvas.drawText(value, leftBorder + w - padH - paints.paintPointerValue.measureText(value), y, paints.paintPointerValue)
        }

        fun scale(v: Float): Float {
            return when {
                enabledOld && enabledNew -> 1f
                !enabledOld && !enabledNew -> 0f
                !enabledOld && enabledNew -> 1f - v
                else -> v
            }
        }
    }

    class Arrow(val size: Int = 0) {
        private val points = floatArrayOf(
            0f, 0f,
            0.5f, 0.5f,
            0f, 1f
        )

        private val path = Path()

        fun draw(left: Float, top: Float, canvas: Canvas, paint: Paint) {
            path.reset()
            path.moveTo(points[0] * size + left, points[1] * size + top)
            for (i in 2 until points.size step 2) {
                path.lineTo(points[i] * size + left, points[i + 1] * size + top)
            }
            canvas.drawPath(path, paint)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}

fun Int.toBoolean(): Boolean = this != 0
fun Boolean.toInt(): Int = if (this) 1 else 0
fun Float.toAlpha(): Int = (255 * this).toInt()