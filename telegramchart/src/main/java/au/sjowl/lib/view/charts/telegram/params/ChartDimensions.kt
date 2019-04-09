package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

class ChartDimensions(context: Context) {
    val axisTextHeight = context.sp(12).toFloat()
    val gridWidth = 3f
    val arrowWidth = 6f
    val pointerTitle = context.sp(12).toFloat()
    val pointerValueText = context.sp(14).toFloat()
    val pointerNameText = context.sp(14).toFloat()
    val pointerRadius = context.dip(10).toFloat()
    val pointerArrowWidth = context.dip(32).toFloat()
    val pointerArrowSize = context.dip(10).toFloat()
    val pointerPadding = context.dip(10).toFloat()
    val checkboxTitle = context.sp(14).toFloat()
    val checkboxIconSize = context.dip(24)
    val checkboxTickWidth = context.dip(2).toFloat()
    val checkboxPaddingVertical = context.dip(13)
    val checkboxPaddingHorizontal = context.dip(7)
    val overviewTouchWidth = context.dip(10)
    val chartPaddingTop = context.dip(20)
    val chartPaddingTextBottom = context.dip(6)
    val chartPaddingHorizontal = context.dip(16)
    val chartPointerCircleRadius = context.dip(5).toFloat()
    val checkboxMargin = context.dip(5)
    val checkboxBorder = context.dip(1.7f).toFloat()

    val overviewPaddingVertical = context.dip(4)
    val overviewRadiusBorder = context.dip(6).toFloat()
    val overviewRadiusWindow = context.dip(6).toFloat()
    val overviewWindowBorder = context.dip(10)
    val overviewWindowOffset = context.dip(1)
    val overviewKnobWidth = context.dip(1f).toFloat()
    val overviewKnobHeight = context.dip(10).toFloat()
}