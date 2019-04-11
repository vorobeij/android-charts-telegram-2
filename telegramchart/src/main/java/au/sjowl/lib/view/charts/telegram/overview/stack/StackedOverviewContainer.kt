package au.sjowl.lib.view.charts.telegram.overview.stack

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class StackedOverviewContainer : BaseChartOverviewContainer {

    override fun init() {
        chart = StackedBarOverviewChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcStackedChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}