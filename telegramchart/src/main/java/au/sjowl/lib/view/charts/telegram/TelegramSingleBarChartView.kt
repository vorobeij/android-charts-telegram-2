package au.sjowl.lib.view.charts.telegram

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.setVisible
import kotlinx.android.synthetic.main.chart_single_bar.view.*

class TelegramSingleBarChartView : TelegramChartView {

    override val layoutId: Int get() = R.layout.chart_single_bar

    override fun onZoom(chartsData: ChartsData, zoomIn: Boolean) {
        super.onZoom(chartsData, zoomIn)
        animateContainer(chartOverviewX, show = chartsData.isZoomed)
        animateContainer(chartNames, show = chartsData.isZoomed)
    }

    private fun init() {
        chartOverviewX.setVisible(!chartsData.isZoomed)
        chartNames.setVisible(chartsData.isZoomed)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}