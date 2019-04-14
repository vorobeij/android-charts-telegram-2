package au.sjowl.lib.view.charts.telegram.params

import android.view.animation.AccelerateInterpolator

object ChartConfig {
    const val animDuration = 200L
    val yIntervals = 5

    fun interpolator() = AccelerateInterpolator()
}