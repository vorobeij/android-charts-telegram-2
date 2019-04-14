package au.sjowl.lib.view.charts.telegram.chart.stack

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisY
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisYView

class StackAxisView : AxisYView {

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