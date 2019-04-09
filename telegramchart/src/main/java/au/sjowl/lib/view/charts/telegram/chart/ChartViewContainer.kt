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
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChartViewContainer : FrameLayout, ThemedView, AnimView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            axisY.chartsData = value
            pointerPopup.chartsData = value
            chart.chartsData = value
            drawPointer = false

            onTimeIntervalChanged()
        }

    var onPopupClicked: (() -> Unit)? = null

    private var chart = ChartView(context)

    private val pointerPopup = ChartPointerPopup(context)

    private val axisY = AxisY(context)

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
        axisY.updateTheme(colors)
        chart.updateTheme(colors)
        pointerPopup.updateTheme(colors)
    }

    override fun updateStartPoints() {
        axisY.updateStartPoints()
        chart.updateStartPoints()
    }

    override fun updateFinishState() {
        adjustValueRange()
        axisY.updateFinishState()
        chart.updateFinishState()
        pointerPopup.update()
    }

    override fun onAnimateValues(v: Float) {
        axisY.onAnimateValues(v)
        chart.onAnimateValues(v)
    }

    fun onTimeIntervalChanged() {
        drawPointer = false
        adjustValueRange()
        axisY.onTimeIntervalChanged()
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

    private fun adjustValueRange() {
        val columns = chartsData.columns.values
        columns.forEach { it.calculateBorders(chartsData.timeIndexStart, chartsData.timeIndexEnd) }
        val enabled = columns.filter { it.enabled }
        val chartsMin = enabled.minBy { it.windowMin }?.windowMin ?: 0
        val chartsMax = enabled.maxBy { it.windowMax }?.windowMax ?: 100
        axisY.adjustValuesRange(chartsMin, chartsMax)
    }

    private fun init() {
        addView(chart)
        addView(axisY)
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