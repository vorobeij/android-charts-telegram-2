package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.ChartPointerPopup
import au.sjowl.lib.view.charts.telegram.other.getTextBounds

class PercentagePointerPopup : ChartPointerPopup {

    private var percentsOffset = 0f

    override fun onDraw(canvas: Canvas) {
        measure()
        restrictX()

        canvas.drawRoundRect(leftBorder, pad, leftBorder + w, h + pad, rectRadius, rectRadius, paints.paintPointerBackground)

        // draw title
        paints.paintPointerTitle.getTextBounds(title, r1)
        var y = 2 * pad + r1.height()
        canvas.drawText(title, leftBorder + pad, y, paints.paintPointerTitle)

        val h0 = itemHeight()
        // draw items
        items.forEach {
            paints.paintPointerValue.color = it.color
            paints.paintPointerValue.getTextBounds(it.value, r1)
            paints.paintPointerName.getTextBounds(it.chartName, r2)

            y += h0 + pad
            val percents = "${it.percent}% "
            canvas.drawText(percents, leftBorder + pad + percentsOffset - paints.paintPointerTitle.measureText(percents), y, paints.paintPointerTitle)
            canvas.drawText(it.chartName, leftBorder + pad + percentsOffset, y, paints.paintPointerName)
            canvas.drawText(it.value, leftBorder + w - pad - r1.width(), y, paints.paintPointerValue)
        }

        if (chartsData.canBeZoomed) {
            arrow.draw(leftBorder + w - pad - arrow.size / 2, 2 * pad, canvas, paints.paintArrow)
        }
    }

    override fun measure() {
        val maxPercent = items.maxBy { it.percent }?.percent ?: 0
        percentsOffset = paints.paintPointerTitle.measureText("$maxPercent% ")
        val itemWidh = (items.map {
            paints.paintPointerValue.measureText(it.value) + paints.paintPointerName.measureText(it.chartName)
        }.max() ?: 0f) + percentsOffset
        w = Math.max(
            2 * pad + itemWidh,
            paints.paintPointerTitle.measureText(title) + arrowWidth
        ) + 2 * pad

        val h0 = itemHeight()
        h = pad + h0 + items.size * (h0 + pad) + pad + pad / 2
    }

    override fun updatePoints(measuredWidth: Int) {
        this.mw = measuredWidth

        title = timeFormatter.format(chartsData.times[timeIndex])

        val total = chartsData.sums[timeIndex]
        items = chartsData.charts.filter { it.enabled }
            .map { ChartPoint(it.name, it.values[timeIndex].toString(), it.color, 100 * it.values[timeIndex] / total) }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}