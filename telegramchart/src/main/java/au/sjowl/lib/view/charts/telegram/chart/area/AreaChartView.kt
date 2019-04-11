package au.sjowl.lib.view.charts.telegram.chart.area

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseOverviewChartView

class AreaChartView : BaseOverviewChartView {
    override fun calcExtremums() = chartsData.calcAreaWindowExtremums()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}