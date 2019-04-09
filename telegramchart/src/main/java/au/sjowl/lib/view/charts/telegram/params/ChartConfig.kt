package au.sjowl.lib.view.charts.telegram.params

import android.view.animation.AccelerateInterpolator

object ChartConfig {
    const val animDuration = 500L
    fun interpolator() = AccelerateInterpolator()
}