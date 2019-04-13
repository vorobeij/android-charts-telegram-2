package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.graphics.Canvas
import android.view.View

class ScaledYAxisRight(
    v: View
) : ScaledYAxis(1, v) {

    override val drawGrid = false

    override fun drawMarks(canvas: Canvas) {
        textOffset = v.width - chartLayoutParams.paddingHorizontal * 1f - paints.paintChartText.measureText("2000")
        paints.paintChartText.color = chart.color
        super.drawMarks(canvas)
    }
}