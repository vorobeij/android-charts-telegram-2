package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.graphics.Canvas
import android.view.View

class ScaledYAxisLeft(
    v: View
) : ScaledYAxis(0, v) {

    override fun drawMarks(canvas: Canvas) {
        paints.paintChartText.color = chart.color
        super.drawMarks(canvas)
    }
}