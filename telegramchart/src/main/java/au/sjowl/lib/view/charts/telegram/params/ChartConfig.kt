package au.sjowl.lib.view.charts.telegram.params

import android.view.animation.AccelerateDecelerateInterpolator

object ChartConfig {
    const val animDuration = 500L
    fun interpolator() = AccelerateDecelerateInterpolator()
}