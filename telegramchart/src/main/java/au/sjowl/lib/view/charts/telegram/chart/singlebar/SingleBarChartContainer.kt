package au.sjowl.lib.view.charts.telegram.chart.singlebar

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.chart.stack.StackPointerPopup

class SingleBarChartContainer : BaseChartContainer {

    override fun init() {
        chart = SingleBarChartView(context)
        pointerPopup = StackPointerPopup(context)
        super.init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}