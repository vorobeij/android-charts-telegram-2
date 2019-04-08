package au.sjowl.lib.view.charts.telegram.overview

import android.content.Context
import org.jetbrains.anko.dip

class OverviewLayoutParams(context: Context) {

    var w = 0f
        get() = field - 2 * paddingHorizontal

    var h = 0f

    val paddingBottom = context.dip(4)

    val paddingTop = context.dip(4)

    val dip = context.dip(1)

    val radiusBorder = context.dip(8).toFloat()

    val radiusWindow = context.dip(8).toFloat()

    val windowBorder = context.dip(16)

    val paddingHorizontal = windowBorder / 2

    val knobWidh = context.dip(1.4f).toFloat()

    val knobHeight: Float = context.dip(16).toFloat()
}