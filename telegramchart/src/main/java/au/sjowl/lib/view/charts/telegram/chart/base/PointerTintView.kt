package au.sjowl.lib.view.charts.telegram.chart.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView

open class PointerTintView : View, ThemedView {
    var chartsData = ChartsData()

    // hack
    var chart: BaseChartView? = null

    override fun updateTheme() {
    }

    open fun updatePoints() {
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}