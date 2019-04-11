package au.sjowl.lib.view.charts.telegram.chart.singlebar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView

class SingleBarChartView : BaseOverviewChartView {
    override fun calcExtremums() = chartsData.calcSingleBarChartExtremums()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}