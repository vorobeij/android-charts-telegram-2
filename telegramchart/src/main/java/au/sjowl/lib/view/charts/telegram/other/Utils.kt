package au.sjowl.lib.view.charts.telegram.other

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import au.sjowl.lib.view.charts.telegram.BuildConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.system.measureNanoTime

suspend fun <A, B> Collection<A>.parallelMap(
    block: suspend (A) -> B
): Collection<B> {
    return map {
        // Use async to start a coroutine for each item
        GlobalScope.async {
            block(it)
        }
    }.map {
        // We now have a map of Deferred<T> so we await() each
        it.await()
    }
}

inline fun View.contains(px: Int, py: Int): Boolean {
    return px > x && px < x + measuredWidth && py > y && py < y + measuredHeight
}

inline fun View.contains(px: Float, py: Float): Boolean {
    return px > x && px < x + width && py > y && py < y + height
}

inline fun View.contains(event: MotionEvent): Boolean {
    return contains(event.x + x, event.y + y)
}

fun Context.getColorFromAttr(attrId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

inline fun Paint.getTextBounds(text: String, rect: Rect) = getTextBounds(text, 0, text.length, rect)

fun measureDrawingMs(msg: String, block: (() -> Unit)) {
    val t = measureNanoTime {
        block.invoke()
    }
    SLog.d("$msg draw %.3f".format(t / 1000000f))
}

fun measureSpeedMs(msg: String, block: (() -> Unit)) {
    val t = measureNanoTime {
        block.invoke()
    }
    println("$msg %.3f".format(t / 1000000f))
}

fun View.setVisible(vis: Boolean) {
    visibility = if (vis) View.VISIBLE else View.GONE
}

fun View.tint(color: Int) {
    setLayerType(View.LAYER_TYPE_HARDWARE, Paint().apply {
        colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    })
}

fun ConstraintLayout.constrain(block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    block.invoke(cs)
    cs.applyTo(this)
}

fun ConstraintLayout.constrain(transition: Transition, block: ((cs: ConstraintSet) -> Unit)) {
    val cs = ConstraintSet()
    cs.clone(this)
    beginDelayedTransitionCompat(this, transition)
    block.invoke(cs)
    cs.applyTo(this)
}

fun Canvas.drawCompatRoundRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, paint: Paint) {
    if (android.os.Build.VERSION.SDK_INT >= 21) {
        drawRoundRect(left, top, right, bottom, rx, ry, paint)
    } else {
        drawRect(left, top, right, bottom, paint)
    }
}

object SLog {
    fun d(msg: String) = if (BuildConfig.DEBUG) Log.d("isj", msg) else 0
}

fun beginDelayedTransitionCompat(viewGroup: ViewGroup, transition: Transition) {
    if (android.os.Build.VERSION.SDK_INT >= 19) {
        TransitionManager.beginDelayedTransition(viewGroup, transition)
    }
}