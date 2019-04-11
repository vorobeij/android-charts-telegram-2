package au.sjowl.lib.view.charts.telegram.overview.singlebar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class SingleBarOverviewContainer : BaseChartOverviewContainer {
    override fun init() {
        chart = SingleBarOverviewChartView(context)
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