package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.ChartPointerPopup
import au.sjowl.lib.view.charts.telegram.chart.base.toAlpha
import au.sjowl.lib.view.charts.telegram.other.drawCompatRoundRect
import au.sjowl.lib.view.charts.telegram.other.getTextBounds

class PercentagePointerPopup : ChartPointerPopup {

    private var percentsOffset = 0f

    override fun onDraw(canvas: Canvas) {
        restrictX()
        updatePoints()
        canvas.drawCompatRoundRect(leftBorder, padV, leftBorder + w, h + padV, rectRadius, rectRadius, paints.paintPointerBackground)

        // draw title
        paints.paintPointerTitle.getTextBounds(title, r1)
        var y = 2f * padV + r1.height()
        canvas.drawText(title, leftBorder + padH, y, paints.paintPointerTitle)

        val h0 = itemHeight()
        // draw items
        items.forEach {
            paints.paintPointerValue.alpha = it.scale(animValue).toAlpha()
            paints.paintPointerName.alpha = it.scale(animValue).toAlpha()
            paints.paintPointerValue.textSize = paints.dimensions.pointerValueText * it.scale(animValue)
            paints.paintPointerName.textSize = paints.dimensions.pointerNameText * it.scale(animValue)

            paints.paintPointerTitle.alpha = it.scale(animValue).toAlpha()
            paints.paintPointerTitle.textSize = paints.dimensions.pointerTitle * it.scale(animValue)

            paints.paintPointerValue.color = it.color
            paints.paintPointerValue.getTextBounds(it.value, r1)
            paints.paintPointerName.getTextBounds(it.chartName, r2)

            y += (h0 + padV) * it.scale(animValue)
            val percents = "${it.percent}% "
            canvas.drawText(percents, leftBorder + padH + percentsOffset - paints.paintPointerTitle.measureText(percents), y, paints.paintPointerTitle)
            canvas.drawText(it.chartName, leftBorder + padH + percentsOffset, y, paints.paintPointerName)
            canvas.drawText(it.value, leftBorder + w - padH - r1.width(), y, paints.paintPointerValue)
        }

        if (chartsData.canBeZoomed) {
            arrow.draw(leftBorder + w - padH - arrow.size / 2, 2f * padV, canvas, paints.paintArrow)
        }
    }

    override fun measure() {
        val maxPercent = items.maxBy { it.percent }?.percent ?: 0
        percentsOffset = paints.paintPointerTitle.measureText("$maxPercent% ")
        val itemWidh = (items.map {
            paints.paintPointerValue.measureText(it.value) + paints.paintPointerName.measureText(it.chartName)
        }.max() ?: 0f) + percentsOffset
        w = Math.max(
            2f * padH + itemWidh,
            paints.paintPointerTitle.measureText(title) + arrowWidth
        ) + 2f * padH

        val h0 = itemHeight()
        val valuesHeight = items.map { point -> (h0 + padV) * point.scale(animValue) }.sum()
        h = padV + h0 + valuesHeight + padV + padV / 2

        setMeasuredDimension((w * 1.3f).toInt(), (h * 1.3f).toInt())
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}