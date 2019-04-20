package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ChartAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.other.ThemedView

open class PointerTintView : View, ThemedView {

    var chartsData = ChartsData()

    // hack
    var chart: BaseChartView? = null

    private val animator = ChartAnimatorWrapper(
        onStart = {
        },
        onAnimate = { value ->
            invalidate()
        })

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun updateTheme() = Unit

    open fun updatePoints() = Unit

    fun onChartStateChanged() {
        animator.start()
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}