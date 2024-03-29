package au.sjowl.lib.view.charts.telegram.overview.scroll

import android.graphics.RectF

internal class OverviewRectangles(
    private val touchWidth: Int
) {

    val timeWindow = RectF()

    val bgLeft = RectF()

    val bgRight = RectF()

    val border = RectF()

    val windowBorderLeft = RectF()

    val windowBorderRight = RectF()

    val timeWindowClip = RectF()

    private val borderLeft = RectF()

    private val borderRight = RectF()

    fun setTimeWindow(left: Float, right: Float, padding: Float) {
        timeWindow.right = right
        timeWindow.left = left

        timeWindowClip.left = timeWindow.left - padding
        timeWindowClip.right = timeWindow.right + padding

        windowBorderLeft.left = timeWindowClip.left
        windowBorderLeft.right = timeWindow.left + padding
        windowBorderRight.right = timeWindowClip.right
        windowBorderRight.left = timeWindow.right - padding
    }

    fun reset(left: Float, top: Float, right: Float, bottom: Float, paddingWindowV: Int, paddingWindowH: Int) {
        timeWindow.top = top - paddingWindowV
        bgLeft.top = top
        bgRight.top = top
        borderLeft.top = top
        borderRight.top = top
        windowBorderLeft.top = top - paddingWindowV
        windowBorderRight.top = top - paddingWindowV

        timeWindow.bottom = bottom + paddingWindowV
        bgLeft.bottom = bottom
        bgRight.bottom = bottom
        borderLeft.bottom = bottom
        borderRight.bottom = bottom
        windowBorderLeft.bottom = bottom + paddingWindowV
        windowBorderRight.bottom = bottom + paddingWindowV

        bgLeft.left = left
        bgRight.right = right

        timeWindowClip.left = timeWindow.left
        timeWindowClip.top = top - paddingWindowV
        timeWindowClip.right = timeWindow.right
        timeWindowClip.bottom = bottom + paddingWindowV

        border.left = left
        border.top = top
        border.right = right
        border.bottom = bottom
    }

    fun updateTouch() {
        borderLeft.left = timeWindow.left - touchWidth
        borderLeft.right = timeWindow.left + touchWidth

        borderRight.left = timeWindow.right - touchWidth
        borderRight.right = timeWindow.right + touchWidth

        timeWindowClip.left = borderLeft.left
        timeWindowClip.right = borderRight.left
    }

    fun getTouchMode(x: Float, y: Float): Int {
        return when {
            borderLeft.contains(x, y) -> ChartOverviewScrollView.TOUCH_SCALE_LEFT
            borderRight.contains(x, y) -> ChartOverviewScrollView.TOUCH_SCALE_RIGHT
            timeWindow.contains(x, y) -> ChartOverviewScrollView.TOUCH_DRAG
            else -> ChartOverviewScrollView.TOUCH_NONE
        }
    }
}