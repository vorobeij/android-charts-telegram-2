package au.sjowl.lib.view.charts.telegram.chart

class HistoryRange {
    var minStart = 0
    var minEnd = 0
    var maxStart = 0
    var maxEnd = 0
    val startInterval get() = maxStart - minStart
    val endInterval get() = maxEnd - minEnd
    val deltaInterval get() = endInterval - startInterval
    val deltaMin get() = minEnd - minStart
}