package au.sjowl.lib.view.charts.telegram.chart.pie

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData

class PieChartView : BaseChartView {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            (charts[0] as PieChart).select(event)
            invalidate()
        }

        return true
    }

    override fun provideChart(it: ChartData, value: ChartsData): AbstractChart {
        return PieChart(it, value, chartLayoutParams)
    }

    override fun addCharts() {
        charts.clear()
        charts.add(provideChart(chartsData.charts[0], chartsData).apply {
            updateTheme(context)
        })
    }

    override fun calcExtremums() = Unit

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}