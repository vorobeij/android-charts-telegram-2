package au.sjowl.lib.view.charts.telegram.chart.pointer

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
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.getTextBounds
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartDimensions
import au.sjowl.lib.view.charts.telegram.time.TimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale

class ChartPointerPopup : View, ThemedView {

    var paints = ChartPointerPaints(context)

    var chartsData = ChartsData()
        set(value) {
            field = value
            timeFormatter = if (value.isZoomed) HourFormatter() else DayFormatter()
        }

    private var title = ""

    private var items = listOf<ChartPoint>()

    private val dimensions = ChartDimensions(context)

    private var rectRadius = dimensions.pointerRadius

    private var timeIndex = 0

    private var w = 0f

    private var h = 0f

    private var x1 = 0f

    private val r1 = Rect()

    private val r2 = Rect()

    private var pad = dimensions.pointerPadding

    private var mw = 0

    private val arrowWidth = dimensions.pointerArrowWidth

    private val arrow = Arrow(dimensions.pointerArrowSize.toInt())

    private var timeFormatter: TimeFormatter = DayFormatter()

    override fun onDraw(canvas: Canvas) {
        measure()
        restrictX()

        canvas.drawRoundRect(x1, pad, x1 + w, h + pad, rectRadius, rectRadius, paints.paintPointerBackground)

        // draw title
        paints.paintPointerTitle.getTextBounds(title, r1)
        var y = 2 * pad + r1.height()
        canvas.drawText(title, x1 + pad, y, paints.paintPointerTitle)

        // draw items
        items.forEach {
            paints.paintPointerValue.color = it.color
            paints.paintPointerValue.getTextBounds(it.value, r1)
            paints.paintPointerName.getTextBounds(it.chartName, r2)

            y += r2.height() + pad
            canvas.drawText(it.chartName, x1 + pad, y, paints.paintPointerName)
            canvas.drawText(it.value, x1 + w - pad - r1.width(), y, paints.paintPointerValue)
        }

        if (chartsData.canBeZoomed) {
            arrow.draw(x1 + w - pad - arrow.size / 2, 2 * pad, canvas, paints.paintArrow)
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

    class ChartPointerPaints(context: Context) : BasePaints(context) {
        val paintArrow = paint().apply {
            color = colors.gridLines
            style = Paint.Style.STROKE
            strokeWidth = dimensions.arrowWidth
            alpha = 25
            strokeCap = Paint.Cap.ROUND
        }

        val paintPointerBackground = paint().apply {
            color = colors.pointer
            setShadowLayer(5f, 0f, 2f, colors.pointerShadow)
        }

        val paintPointerTitle = paint().apply {
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            color = colors.text
            textSize = dimensions.pointerTitle
        }

        val paintPointerValue = paint().apply {
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            textSize = dimensions.pointerValueText
        }

        val paintPointerName = paint().apply {
            textSize = dimensions.pointerNameText
            color = colors.text
        }
    }

    override fun updateTheme() {
        paints = ChartPointerPaints(context)
        invalidate()
    }

    fun isInBounds(x: Float, y: Float): Boolean {
        return isVisible && x in x1..x1 + w && y in 2 * pad..2 * pad + h
    }

    fun updatePoints(x: Float, measuredWidth: Int) {
        this.mw = measuredWidth
        timeIndex = chartsData.pointerTimeIndex

        val time = chartsData.times[timeIndex]
        title = timeFormatter.format(time)
        items = chartsData.charts.filter { it.enabled }
            .map { ChartPoint(it.name, it.values[timeIndex].toString(), it.color) }

        measure()
        this.x1 = x - w / 2
        restrictX()
    }

    private fun restrictX() {
        if (x1 < pad) x1 = pad
        val max = mw - w - pad
        if (x1 > max) x1 = max
    }

    private fun measure() {
        w = Math.max(
            items.map {
                paints.paintPointerValue.measureText(it.value) + paints.paintPointerName.measureText(it.chartName)
            }.max() ?: 0f,
            paints.paintPointerTitle.measureText(title) + arrowWidth
        ) + 2 * pad

        h = pad * 2
        paints.paintPointerTitle.getTextBounds(title, r1)
        h += r1.height() + items.map {
            paints.paintPointerName.getTextBounds(items[0].chartName, r1)
            r1.height() + pad
        }.sum()
    }

    private class DayFormatter : TimeFormatter() {
        override val dateFormat get() = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    }

    private class HourFormatter : TimeFormatter() {
        override val dateFormat get() = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
    }

    private data class ChartPoint(
        val chartName: String,
        val value: String,
        val color: Int
    )

    private class Arrow(val size: Int = 0) {
        private val points = floatArrayOf(
            0f, 0f,
            0.5f, 0.5f,
            0f, 1f
        )

        private val path = Path()

        fun draw(left: Float, top: Float, canvas: Canvas, paint: Paint) {
            path.reset()
            val dx = left
            val dy = top
            path.moveTo(points[0] * size + dx, points[1] * size + dy)
            for (i in 2 until points.size step 2) {
                path.lineTo(points[i] * size + dx, points[i + 1] * size + dy)
            }
            canvas.drawPath(path, paint)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
