package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

abstract class RenderView : View {

    private val renderThread = RenderThread(this) { canvas ->
        onDrawAtRender(canvas)
    }.apply { start() }

    abstract fun onDrawAtRender(canvas: Canvas)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        renderThread.createNewBitmap(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        renderThread.drawFrame(canvas)
    }

    override fun invalidate() {
        super.invalidate()
        if (renderThread.hasBitmap) {
            renderThread.hasBitmap = false
        } else {
            renderThread.renderNew()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}