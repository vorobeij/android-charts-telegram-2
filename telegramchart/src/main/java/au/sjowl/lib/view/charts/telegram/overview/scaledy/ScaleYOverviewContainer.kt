package au.sjowl.lib.view.charts.telegram.overview.scaledy

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class ScaleYOverviewContainer : BaseChartOverviewContainer {

    override fun init() {
        chart = ScaleYOverviewChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcYScaledChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}