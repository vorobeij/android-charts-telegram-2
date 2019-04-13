package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisYView

class ScaleYAxisView : AxisYView {

    private fun init() {
        axises = arrayListOf(ScaledYAxisLeft(this), ScaledYAxisRight(this))
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
}