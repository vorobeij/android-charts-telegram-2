package au.sjowl.lib.view.charts.telegram.chart.stack

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.ChartPointerPopup

class StackPointerPopup : ChartPointerPopup {

    private val tint = TintRectangles()

    override fun onDraw(canvas: Canvas) {
        tint.draw(canvas, chartsData.pointerTimeX, chartsData.barHalfWidth, measuredWidth, measuredHeight, paints.paintTint)
        super.onDraw(canvas)
    }

    class TintRectangles {
        val left = RectF()
        val right = RectF()

        fun draw(canvas: Canvas, center: Float, margin: Float, width: Int, height: Int, paint: Paint) {
            with(left) {
                left = 0f
                top = 0f
                right = center - margin
                bottom = height * 1f
            }
            with(right) {
                left = center + margin
                top = 0f
                right = width * 1f
                bottom = height * 1f
            }
            canvas.drawRect(left, paint)
            canvas.drawRect(right, paint)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}