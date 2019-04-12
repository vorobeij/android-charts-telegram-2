package au.sjowl.lib.view.charts.telegram.chart.stack

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.PointerTintView

class StackTintView : PointerTintView {

    override fun onDraw(canvas: Canvas) {
        chart?.drawPointers(canvas)
    }

    override fun updateTheme() {
        invalidate()
    }

    override fun updatePoints() {
        invalidate() // todo if not showing when points are updated, no sense
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}