package au.sjowl.lib.view.charts.telegram.overview.percentage

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class PercentageOverviewContainer : BaseChartOverviewContainer {
    override fun init() {
        chart = PercentageOverviewChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcPercentageChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}