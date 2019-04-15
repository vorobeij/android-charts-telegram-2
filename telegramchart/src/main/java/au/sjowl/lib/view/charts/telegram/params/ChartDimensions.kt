package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

class ChartDimensions(context: Context) {
    val axisTextHeight = context.sp(12).toFloat()
    val gridWidth = context.dip(1).toFloat()
    val arrowWidth = context.dip(2f).toFloat()
    val overviewLineWidth = context.dip(1).toFloat()
    val pointerTitle = context.sp(12).toFloat()
    val pointerValueText = context.sp(14).toFloat()
    val pointerNameText = context.sp(14).toFloat()
    val pointerRadius = context.dip(4).toFloat()
    val pointerArrowWidth = context.dip(32).toFloat()
    val pointerArrowSize = context.dip(10).toFloat()
    val pointerPaddingVert = context.dip(8).toFloat()
    val pointerPaddingHorizontal = context.dip(12).toFloat()
    val checkboxTitle = context.sp(14).toFloat()
    val checkboxIconSize = context.dip(24)
    val checkboxTickWidth = context.dip(2).toFloat()
    val checkboxPaddingVertical = context.dip(13)
    val checkboxPaddingHorizontal = context.dip(7)
    val overviewTouchWidth = context.dip(10)
    val chartPaddingTop = context.dip(20)
    val chartPaddingTextBottom = context.dip(6)
    val chartPaddingHorizontal = context.dip(16).toFloat()
    val chartPointerCircleRadius = context.dip(5).toFloat()
    val checkboxMargin = context.dip(5)
    val checkboxBorder = context.dip(1.7f).toFloat()

    val overviewScrollPaddingVertical = context.dip(4)
    val overviewPaddingVertical = context.dip(4)
    val overviewRadiusBorder = context.dip(6).toFloat()
    val overviewRadiusWindow = context.dip(6).toFloat()
    val overviewWindowBorder = context.dip(10).toFloat()
    val overviewWindowOffset = context.dip(1)
    val overviewKnobWidth = context.dip(2f).toFloat()
    val overviewKnobHeight = context.dip(10).toFloat()
    val chartLineWidth: Float = context.dip(2).toFloat()
}