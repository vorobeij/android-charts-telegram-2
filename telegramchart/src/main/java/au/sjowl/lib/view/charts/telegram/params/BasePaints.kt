package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Paint
import au.sjowl.lib.view.charts.telegram.chart.ChartDimensions

open class BasePaints(context: Context) {
    val dimensions = ChartDimensions(context)
    val colors = ChartColors(context)
    fun paint() = Paint().apply { isAntiAlias = true }
}