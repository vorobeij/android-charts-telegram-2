package au.sjowl.lib.view.charts.telegram.chart.yscaled

import android.graphics.Canvas
import android.view.View
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisY

open class ScaledYAxis(
    val chartIndex: Int,
    v: View
) : AxisY(v) {

    override val windowMin get() = chart.windowMin

    override val windowMax get() = chart.windowMax

    val chart get() = chartsData.charts[chartIndex]

    override fun ky(): Float = (mh - chartLayoutParams.paddingTop) / chart.windowValueInterval

    override fun drawMarks(canvas: Canvas) {
        if (chart.enabled) {
            super.drawMarks(canvas)
        }
    }
}