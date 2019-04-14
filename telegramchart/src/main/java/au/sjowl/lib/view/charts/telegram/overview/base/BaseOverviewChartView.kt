package au.sjowl.lib.view.charts.telegram.overview.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.overview.scroll.OverviewScrollLayoutParams
import au.sjowl.lib.view.charts.telegram.params.OverviewChartLayoutParams

abstract class BaseOverviewChartView : BaseChartView {

    override val chartLayoutParams = OverviewChartLayoutParams(context)

    private val clipPath = Path()

    private val overviewLayoutParams = OverviewScrollLayoutParams(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        clipPath.reset()
        val d = chartLayoutParams
        val r = overviewLayoutParams.radiusWindow
        val rect = RectF(d.paddingHorizontal, d.paddingTop * 1f, w - d.paddingHorizontal, h * 1f - d.paddingBottom)
        clipPath.addRoundRect(rect, r, r, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(clipPath)
        for (i in charts.size - 1 downTo 0) {
            charts[i].draw(canvas)
        }
        canvas.restore()
    }

    override fun onTimeIntervalChanged() = Unit
    override fun drawPointers(canvas: Canvas) = Unit
    override fun onDrawPointer(draw: Boolean) = Unit
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}