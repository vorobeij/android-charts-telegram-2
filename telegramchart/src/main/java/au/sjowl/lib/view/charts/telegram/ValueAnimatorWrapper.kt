package au.sjowl.lib.view.charts.telegram

import android.animation.Animator
import android.animation.ValueAnimator
import au.sjowl.lib.view.charts.telegram.params.ChartConfig

class ValueAnimatorWrapper(var onAnimate: ((value: Float) -> Unit)) {

    private var animValue = 1f

    private val animator = ValueAnimator().apply {
        duration = ChartConfig.animDuration
        interpolator = ChartConfig.interpolator()
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                removeAllUpdateListeners()
            }

            override fun onAnimationCancel(animation: Animator?) {
                removeAllUpdateListeners()
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        val v = animator.animatedValue as Float
        if (v != animValue) {
            animValue = v
            onAnimate(v)
        }
    }

    fun start() {
        animator.setFloatValues(1f, 0f)
        animator.addUpdateListener(updateListener)
        animator.start()
    }
}