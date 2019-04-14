package au.sjowl.lib.view.charts.telegram.chart.base.axis

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class AxisYView : View, ThemedView {

    open var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axises.forEach { it.chartsData = value }
            updatePoints()
        }

    protected val chartLayoutParams: ChartLayoutParams = ChartLayoutParams(context)

    protected var axises = arrayListOf<AxisY>()

    private val scaleAnimator = ValueAnimatorWrapper(
        onStart = {
            axises.forEach { it.onAnimationScaleStart() }
        },
        onAnimate = { v ->
            if (measuredHeight != 0 && measuredWidth != 0) {
                axises.forEach { it.onAnimateScale(v) }
                invalidate()
            }
        })

    private val scrollAnimator = ValueAnimatorWrapper(
        onStart = {
            axises.forEach { it.onAnimationScrollStart() }
        },
        onAnimate = { v ->
            if (measuredHeight != 0 && measuredWidth != 0) {
                axises.forEach { it.onAnimateScroll(v) }
                invalidate()
            }
        })

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w > 0 && h > 0) updatePoints()
    }

    override fun onDraw(canvas: Canvas) {
        axises.forEach { it.draw(canvas) }
    }

    override fun updateTheme() {
        axises.forEach { it.updateTheme(context) }
        invalidate()
    }

    fun onChartStateChanged() {
        updatePoints()
    }

    fun onTimeIntervalChanged() {
        if (chartsData.scaleInProgress) {
            if (axises.any { it.isIntervalChanged() }) scrollAnimator.start()
        } else {
            updatePoints()
        }
    }

    private fun updatePoints() {
        if (measuredHeight == 0 || measuredWidth == 0) return
        scaleAnimator.start()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}