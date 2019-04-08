package au.sjowl.lib.view.charts.telegram.chart

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import au.sjowl.lib.view.charts.telegram.AnimView
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.setVisible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.wrapContent

class ChartViewContainer : FrameLayout, ThemedView, AnimView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            chart.chartsData = value
            pointerPopup.chartsData = value
            drawPointer = false
        }

    val chart = ChartView(context).apply {
        layoutParams = LayoutParams(matchParent, matchParent)
    }

    val pointerPopup = ChartPointerPopup(context).apply {
        layoutParams = LayoutParams(wrapContent, wrapContent)
    }

    var onPopupClicked: (() -> Unit)? = null

    private var drawPointer = false
        set(value) {
            chart.drawPointer = value
            chart.invalidate()
            pointerPopup.setVisible(value)
            pointerPopup.invalidate()
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                updateTimeIndexFromX(event.x)
                drawPointer = true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                updateTimeIndexFromX(event.x)
                invalidate()
            }
        }
        return true
    }

    override fun updateTheme(colors: ChartColors) {
        chart.updateTheme(colors)
        pointerPopup.updateTheme(colors)
    }

    override fun updateFinishState() {
        chart.updateFinishState()
        pointerPopup.update()
    }

    override fun updateStartPoints() {
        chart.updateStartPoints()
    }

    override fun onAnimateValues(v: Float) {
        chart.onAnimateValues(v)
    }

    fun onTimeIntervalChanged() {
        drawPointer = false
        chart.onTimeIntervalChanged()
    }

    private fun updateTimeIndexFromX(x: Float) {
        val t = chartsData.pointerTimeIndex

        chart.updateTimeIndexFromX(x)

        if (t != chartsData.pointerTimeIndex) {
            pointerPopup.updatePoints(x, measuredWidth)
            invalidate()
        }
    }

    private fun init() {
        addView(chart)
        addView(pointerPopup)
        pointerPopup.onClick { onPopupClicked?.invoke() }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}