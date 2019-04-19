package au.sjowl.lib.view.charts.telegram.chart.pie

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisYView

class PieAxisView : AxisYView {

    private fun init() {
        axises = arrayListOf()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}