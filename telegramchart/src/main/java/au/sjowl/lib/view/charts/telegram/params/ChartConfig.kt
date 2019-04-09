package au.sjowl.lib.view.charts.telegram.params

import android.view.animation.AccelerateDecelerateInterpolator

object ChartConfig {
    const val animDuration = 250L
    fun interpolator() = AccelerateDecelerateInterpolator()
}