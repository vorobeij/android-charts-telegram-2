package au.sjowl.lib.view.charts.telegram.other

import android.animation.Animator
import android.animation.ValueAnimator
import au.sjowl.lib.view.charts.telegram.params.ChartConfig

open class ValueAnimatorWrapper(
    var onStart: (() -> Unit),
    var onAnimate: ((value: Float) -> Unit)
) {

    protected val animator = ValueAnimator().apply {
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

    protected val updateListener = ValueAnimator.AnimatorUpdateListener {
        currentValue = animator.animatedValue as Float
        onAnimate(currentValue)
    }

    private var currentValue = 0f

    open fun start() {
        if (!animator.isStarted) {
            onStart()
            animator.setFloatValues(1f, 0f)
            animator.addUpdateListener(updateListener)
            animator.start()
        }
    }
}

class ChartAnimatorWrapper(
    onStart: (() -> Unit),
    onAnimate: ((value: Float) -> Unit)
) : ValueAnimatorWrapper(onStart, onAnimate) {
    override fun start() {
        onStart()
        animator.setFloatValues(1f, 0f)
        animator.addUpdateListener(updateListener)
        animator.start()
    }
}