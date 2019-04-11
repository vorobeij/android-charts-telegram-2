package au.sjowl.lib.view.charts.telegram.overview.linear

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class LinearOverviewContainer : BaseChartOverviewContainer {

    override fun init() {
        chart = LinearOverviewChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcLinearChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}