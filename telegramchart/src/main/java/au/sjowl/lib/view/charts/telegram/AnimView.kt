package au.sjowl.lib.view.charts.telegram

interface AnimView {
    fun updateStartPoints()
    fun updateFinishState()
    fun onAnimateValues(v: Float)
}