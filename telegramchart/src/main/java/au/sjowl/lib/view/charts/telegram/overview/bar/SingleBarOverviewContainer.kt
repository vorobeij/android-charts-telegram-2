package au.sjowl.lib.view.charts.telegram.overview.bar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer
import au.sjowl.lib.view.charts.telegram.overview.stack.StackedBarOverviewChartView

class SingleBarOverviewContainer : BaseChartOverviewContainer {
    override fun init() {
        chart = StackedBarOverviewChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcSingleBarChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}