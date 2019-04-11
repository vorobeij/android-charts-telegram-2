package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisVert
import au.sjowl.lib.view.charts.telegram.chart.axis.AxisY
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class YScaledAxis : AxisY {

    override var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axises = arrayListOf(AxisVert(chartLayoutParams.yMarks, context, chartLayoutParams))
        }

    override fun adjustValuesRange() {
        chartsData.calcStackedWindowExtremums()
        axises[0].calculateMarks(chartsData.windowMin, chartsData.windowMax)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}