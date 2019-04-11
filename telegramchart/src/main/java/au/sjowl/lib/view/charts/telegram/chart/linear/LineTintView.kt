package au.sjowl.lib.view.charts.telegram.chart.linear

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.PointerTintView
import au.sjowl.lib.view.charts.telegram.params.BasePaints

// todo use it instead of pointers
class LineTintView : PointerTintView {

    private var paints = Paints(context)

    override fun onDraw(canvas: Canvas) {
    }

    override fun updateTheme() {
        paints = Paints(context)
    }

    override fun updatePoints() {
        invalidate()
    }

    class Paints(context: Context) : BasePaints(context) {
        val paintTint = Paint()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}