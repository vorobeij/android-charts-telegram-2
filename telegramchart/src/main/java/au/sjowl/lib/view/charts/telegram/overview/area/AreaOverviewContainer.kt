package au.sjowl.lib.view.charts.telegram.overview.area

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.area.AreaChartView
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer

class AreaOverviewContainer : BaseChartOverviewContainer {
    override fun init() {
        chart = AreaChartView(context)
        super.init()
    }

    override fun calculateExtremums() {
        chartsData.calcAreaChartExtremums()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}