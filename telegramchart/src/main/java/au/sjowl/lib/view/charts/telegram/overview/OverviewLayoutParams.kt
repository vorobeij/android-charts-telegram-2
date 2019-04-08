package au.sjowl.lib.view.charts.telegram.overview

import android.content.Context
import org.jetbrains.anko.dip

class OverviewLayoutParams(context: Context) {

    var w = 0f
        get() = field - 2 * paddingHorizontal

    var h = 0f
        get() = field - 2 * paddingVertical

    val paddingVertical = context.dip(4)

    val dip = context.dip(1)

    val radiusBorder = context.dip(6).toFloat()

    val radiusWindow = context.dip(6).toFloat()

    val windowBorder = context.dip(10)

    val windowOffset = context.dip(1)

    val paddingHorizontal = windowBorder / 2

    val knobWidth = context.dip(1f).toFloat()

    val knobHeight: Float = context.dip(10).toFloat()
}