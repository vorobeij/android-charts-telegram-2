package au.sjowl.lib.view.charts.telegram.chart.stack

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.PointerTintView
import au.sjowl.lib.view.charts.telegram.params.BasePaints

class StackTintView : PointerTintView {

    private val tint = TintRectangles()

    private var paints = Paints(context)

    override fun onDraw(canvas: Canvas) {
        tint.draw(canvas, chartsData.pointerTimeX, chartsData.barHalfWidth, measuredWidth, measuredHeight, paints.paintTint)
    }

    override fun updateTheme() {
        paints = Paints(context)
        invalidate()
    }

    override fun updatePoints() {
        invalidate()
    }

    class Paints(context: Context) : BasePaints(context) {
        val paintTint = Paint().apply {
            shader = LinearGradient(0f, 0f, 0f, 250f, Color.TRANSPARENT, colors.scrollBackground, Shader.TileMode.CLAMP)
        }
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