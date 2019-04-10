package au.sjowl.lib.view.charts.telegram.other

interface AnimView {
    fun updateStartPoints()
    fun updateFinishState()
    fun onAnimateValues(v: Float)
}