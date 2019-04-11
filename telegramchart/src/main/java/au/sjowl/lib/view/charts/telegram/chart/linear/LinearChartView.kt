package au.sjowl.lib.view.charts.telegram.chart.linear

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView

class LinearChartView : BaseChartView {
    override fun calcExtremums() = chartsData.calcLinearWindowExtremums()
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}