package au.sjowl.lib.view.charts.telegram.chart.axis

import android.content.Context
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class AxisStackedBars : AxisY {

    override var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axises = arrayListOf(AxisVert(chartLayoutParams.yMarks, context, chartLayoutParams))
            sums = IntArray(chartsData.time.values.size)
            chartsData.columns.values.forEach { col ->
                for (i in 0 until col.values.size) {
                    sums[i] += col.values[i]
                }
            }
        }

    private var sums: IntArray = IntArray(0)

    override fun adjustValuesRange() {
        chartsData.valueMin = 0
        chartsData.valueMax = chartsMax()
        axises[0].calculateMarks(chartsData.valueMin, chartsData.valueMax)
    }

    private inline fun chartsMax(): Int {
        var max = 0
        for (i in chartsData.timeIndexStart..chartsData.timeIndexEnd) {
            if (sums[i] > max) max = sums[i]
        }
        return max
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}