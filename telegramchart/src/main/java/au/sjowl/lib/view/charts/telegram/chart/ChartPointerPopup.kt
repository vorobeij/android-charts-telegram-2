package au.sjowl.lib.view.charts.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import au.sjowl.lib.view.charts.telegram.DateFormatter
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.getTextBounds
import au.sjowl.lib.view.charts.telegram.params.ChartPaints
import org.jetbrains.anko.dip

class ChartPointerPopup(
    context: Context,
    var paints: ChartPaints
) {

    var chartData = ChartsData()

    var canZoom = true

    private var title = ""

    private var items = listOf<ChartPoint>()

    private var rectRadius = context.dip(10).toFloat()

    private var timeIndex = 0

    private var w = 0f

    private var h = 0f

    private var x = 0f

    private val r1 = Rect()

    private val r2 = Rect()

    private var pad = context.dip(10).toFloat()

    private var mw = 0

    private val arrowWidth = context.dip(32)

    private val arrow = Arrow(context.dip(10))

    fun updatePoints(x: Float, measuredWidth: Int) {
        this.mw = measuredWidth
        timeIndex = chartData.pointerTimeIndex

        val time = chartData.time.values[timeIndex]
        title = DateFormatter.formatEDMYShort(time)
        items = chartData.columns.values.filter { it.enabled }
            .map { ChartPoint(it.name, it.values[timeIndex].toString(), it.color) }

        measure()
        this.x = x - w / 2
        restrictX()
    }

    fun update() = updatePoints(x + w / 2, this.mw)

    fun draw(canvas: Canvas) {
        measure()
        restrictX()

        canvas.drawRoundRect(x, pad, x + w, h + pad, rectRadius, rectRadius, paints.paintPointerBackground)

        // draw title
        paints.paintPointerTitle.getTextBounds(title, r1)
        var y = 2 * pad + r1.height()
        canvas.drawText(title, x + pad, y, paints.paintPointerTitle)

        // draw items
        items.forEach {
            paints.paintPointerValue.color = it.color
            paints.paintPointerName.color = it.color
            paints.paintPointerValue.getTextBounds(it.value, r1)
            paints.paintPointerName.getTextBounds(it.chartName, r2)

            y += r2.height() + pad
            canvas.drawText(it.chartName, x + pad, y, paints.paintPointerName)
            canvas.drawText(it.value, x + w - pad - r1.width(), y, paints.paintPointerValue)
        }

        if (canZoom) {
            arrow.draw(x + w - pad - arrow.size / 2, 2 * pad, canvas, paints.paintArrow)
        }
    }

    private fun restrictX() {
        if (x < pad) x = pad
        val max = mw - w + pad
        if (x > max) x = max
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
}

private data class ChartPoint(
    val chartName: String,
    val value: String,
    val color: Int
)

class Arrow(val size: Int = 0) {
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