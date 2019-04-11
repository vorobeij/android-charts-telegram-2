package au.sjowl.lib.view.charts.telegram.overview.scroll

import android.content.Context
import au.sjowl.lib.view.charts.telegram.params.ChartDimensions
import org.jetbrains.anko.dip

class OverviewScrollLayoutParams(context: Context) {

    val dimensions: ChartDimensions = ChartDimensions(context)

    var w0 = 0f
        get() = field - 2 * paddingHorizontal0

    var h0 = 0f
        get() = field - 2 * paddingVertical

    val dip = context.dip(1)

    val paddingVertical = dimensions.overviewScrollPaddingVertical

    val radiusBorder = dimensions.overviewRadiusBorder

    val radiusWindow = dimensions.overviewRadiusWindow

    val windowBorder = dimensions.overviewWindowBorder

    val windowOffset = dimensions.overviewWindowOffset

    val knobWidth = dimensions.overviewKnobWidth

    val knobHeight = dimensions.overviewKnobHeight

    val paddingHorizontal0 = windowBorder / 2
}