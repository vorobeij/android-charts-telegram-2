package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Paint

open class BasePaints(context: Context) {
    val dimensions = ChartDimensions(context)
    val colors = ChartColors(context)
    fun simplePaint() = Paint()
    fun antiAliasPaint() = Paint().apply { isAntiAlias = true }
}