package au.sjowl.lib.view.charts.telegram.overview

import android.content.Context
import au.sjowl.lib.view.charts.telegram.params.ChartDimensions
import org.jetbrains.anko.dip

class OverviewLayoutParams(context: Context) {
    val dimensions: ChartDimensions = ChartDimensions(context)

    var w = 0f
        get() = field - 2 * paddingHorizontal

    var h = 0f
        get() = field - 2 * paddingVertical

    val dip = context.dip(1)

    val paddingVertical = dimensions.overviewPaddingVertical

    val radiusBorder = dimensions.overviewRadiusBorder

    val radiusWindow = dimensions.overviewRadiusWindow

    val windowBorder = dimensions.overviewWindowBorder

    val windowOffset = dimensions.overviewWindowOffset

    val knobWidth = dimensions.overviewKnobWidth

    val knobHeight = dimensions.overviewKnobHeight

    val paddingHorizontal = windowBorder / 2
}