package au.sjowl.lib.view.charts.telegram.overview.base

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartView
import au.sjowl.lib.view.charts.telegram.params.OverviewChartLayoutParams

abstract class BaseOverviewChartView : BaseChartView {

    override val chartLayoutParams = OverviewChartLayoutParams(context)

    override fun onDraw(canvas: Canvas) {
        charts.forEach { it.draw(canvas) }
    }

    override fun providePaints() = ChartViewPaints(context).apply {
        paintChartLine.strokeWidth = dimensions.overviewLineWidth
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}