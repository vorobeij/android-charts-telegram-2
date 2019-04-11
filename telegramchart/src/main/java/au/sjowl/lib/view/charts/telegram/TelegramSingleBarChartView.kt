package au.sjowl.lib.view.charts.telegram

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.chart_single_bar.view.*

class TelegramSingleBarChartView : TelegramChartView {

    override val layoutId: Int get() = R.layout.chart_single_bar

    override fun onChartsDataChanged() {
        super.onChartsDataChanged()
        // todo do not set data to overview on zoom in - not showing it
        animateContainer(chartOverviewX, show = !chartsData.isZoomed)
        animateContainer(chartNames, show = chartsData.isZoomed)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}