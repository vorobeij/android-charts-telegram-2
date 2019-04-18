package au.sjowl.lib.view.charts.telegram.android

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class SjScrollView : ScrollView {

    private var previousX = 0f

    private var previousY = 0f

    private var blockScrolling = false

    private var doScrolling = false

    private val scrollSlop = 200

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                doScrolling = false
                blockScrolling = false
                super.onTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(x - previousX)
                val dy = Math.abs(y - previousY)

                if (dx > scrollSlop && !doScrolling) {
                    blockScrolling = true
                }

                if (dy > scrollSlop && !blockScrolling) {
                    doScrolling = true
                }

                if (!blockScrolling) {
                    super.onTouchEvent(ev)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!blockScrolling) {
                    super.onTouchEvent(ev)
                }
                doScrolling = false
                blockScrolling = false
                return false
            }
            else -> {
            }
        }
        previousX = x
        previousY = y
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        super.onTouchEvent(ev)
        return true
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}