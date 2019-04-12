package au.sjowl.lib.view.charts.telegram.chart.linear

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.PointerTintView
import au.sjowl.lib.view.charts.telegram.params.BasePaints
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class LineTintView : PointerTintView {

    val chartLayoutParams = ChartLayoutParams(context)

    private var paints = Paints(context)

    override fun onDraw(canvas: Canvas) {
        val x = chartsData.pointerTimeX
        canvas.drawLine(x, measuredHeight * 1f - chartLayoutParams.paddingBottom, x, chartLayoutParams.paddingTop * 1f, paints.paintGrid)
        chart?.drawPointers(canvas)
    }

    override fun updateTheme() {
        paints = Paints(context)
        invalidate()
    }

    override fun updatePoints() {
        invalidate() // todo if not showing when points are updated, no sense
    }

    class Paints(context: Context) : BasePaints(context) {
        val paintGrid = paint().apply {
            color = colors.gridLines
            style = Paint.Style.STROKE
            strokeWidth = dimensions.gridWidth
            strokeCap = Paint.Cap.ROUND
            alpha = (0.1f * 255).toInt()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}