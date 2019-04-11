package au.sjowl.lib.view.charts.telegram.overview.scroll

internal class TouchHelper {
    var timeStartDownIndex = 0
    var timeEndDownIndex = 0
    var xDown = 0f
    var touchMode: Int = ChartOverviewScrollView.TOUCH_NONE
}