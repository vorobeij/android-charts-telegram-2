package au.sjowl.lib.view.charts.telegram.overview.base

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.overview.linear.LinearOverviewChartView
import au.sjowl.lib.view.charts.telegram.overview.scroll.ChartOverviewScrollView

abstract class BaseChartOverviewContainer : FrameLayout, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            calculateExtremums()
            chart.chartsData = value
            overviewScroll.chartsData = value
        }

    var onTimeIntervalChanged: (() -> Unit) = {}
        set(value) {
            field = value
            overviewScroll.onTimeIntervalChanged = value
        }

    protected open var chart: BaseOverviewChartView = LinearOverviewChartView(context)

    private val overviewScroll = ChartOverviewScrollView(context)

    abstract fun calculateExtremums()

    override fun updateTheme() {
        chart.updateTheme()
        overviewScroll.updateTheme()
    }

    fun onChartStateChanged() {
        calculateExtremums()
        chart.onChartStateChanged()
    }

    protected open fun init() {
        addView(chart)
        addView(overviewScroll)
        overviewScroll.onTimeIntervalChanged = onTimeIntervalChanged
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}