package au.sjowl.lib.view.charts.telegram

import android.animation.ValueAnimator
import au.sjowl.lib.view.charts.telegram.params.ChartConfig

class ValueAnimatorWrapper(var onAnimate: ((value: Float) -> Unit)) {
    private var animValue = 1f

    private val animator = ValueAnimator().apply {
        setFloatValues(1f, 0f)
        duration = ChartConfig.animDuration
        interpolator = ChartConfig.interpolator()
        addUpdateListener {
            val v = animatedValue as Float
            if (v != animValue) {
                animValue = v
                onAnimate(v)
            }
        }
    }

    fun start() = animator.start()

    fun destroy() {
        animator.cancel()
        animator.removeAllUpdateListeners()
    }
}