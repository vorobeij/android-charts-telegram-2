package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.graphics.Canvas
import android.view.View
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisY

class PercentageAxis(
    v: View
) : AxisY(v) {

    override fun onAnimationScrollStart() = Unit
    override fun onAnimateScroll(value: Float) = Unit

    override fun onAnimationScaleStart() {
        super.onAnimationScaleStart()
        pointsTo.calcCurrent(0f, v.width, chartLayoutParams.paddingHorizontal, chartLayoutParams.paddingHorizontal)
    }

    override fun onAnimateScale(value: Float) = Unit

    override fun drawGrid(canvas: Canvas) {
        paints.paintGrid.alpha = 25
        canvas.drawLines(pointsTo.gridPoints, paints.paintGrid)
    }

    override fun drawMarks(canvas: Canvas) {
        paints.paintChartText.alpha = 255
        drawTitlesTo(canvas)
    }
}