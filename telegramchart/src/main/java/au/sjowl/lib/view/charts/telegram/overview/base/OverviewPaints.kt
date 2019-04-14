package au.sjowl.lib.view.charts.telegram.overview.base

import android.content.Context
import android.graphics.Paint
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart

class OverviewPaints(context: Context) : AbstractChart.ChartPaints(context) {
    override val paintChartLine = simplePaint().apply {
        strokeWidth = dimensions.overviewLineWidth
        style = Paint.Style.STROKE
    }
}
