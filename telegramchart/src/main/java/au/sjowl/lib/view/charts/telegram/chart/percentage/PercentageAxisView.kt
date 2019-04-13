package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisY
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisYView

class PercentageAxisView : AxisYView {

    private fun init() {
        axises = arrayListOf(AxisY(this))
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}