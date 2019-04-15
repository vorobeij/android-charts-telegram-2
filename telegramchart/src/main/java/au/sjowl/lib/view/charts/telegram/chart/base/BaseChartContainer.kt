package au.sjowl.lib.view.charts.telegram.chart.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import au.sjowl.lib.view.charts.telegram.chart.base.axis.AxisYView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.setVisible
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Base class for all chart containers
 */
// todo remove linear-specific stuff
abstract class BaseChartContainer : FrameLayout, ThemedView {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            onChartsDataChanged()
        }

    var onPopupClicked: (() -> Unit)? = null

    protected open var axisY: AxisYView? = null

    protected open var chart: BaseChartView? = null

    protected open var tint: PointerTintView? = null

    protected open var pointerPopup: ChartPointerPopup? = null

    private var drawPointer = false
        set(value) {
            chart?.onDrawPointer(value)
            pointerPopup?.setVisible(value)
            tint?.setVisible(value)
            pointerPopup?.invalidate()
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
                invalidate()
            }
        }
        return true
    }

    override fun updateTheme() {
        chart?.updateTheme()
        tint?.updateTheme()
        axisY?.updateTheme()
        pointerPopup?.updateTheme()
    }

    open fun onChartsDataChanged() {
        chart?.chartsData = chartsData
        pointerPopup?.chartsData = chartsData
        axisY?.chartsData = chartsData
        tint?.chartsData = chartsData
        drawPointer = false
    }

    open fun onChartStateChanged() { // todo replace with observables of chartsData
        chart?.onChartStateChanged()
        axisY?.onChartStateChanged()
        pointerPopup?.onChartStateChanged()
        drawPointer = false
    }

    open fun onTimeIntervalChanged() { // todo replace with observables of chartsData
        chart?.onTimeIntervalChanged()
        axisY?.onTimeIntervalChanged()
        drawPointer = false
    }

    protected open fun init() {
        addView(chart)
        tint?.let { tint ->
            addView(tint)
            tint.chart = this@BaseChartContainer.chart
        }
        addView(axisY)
        addView(pointerPopup)
        pointerPopup?.onClick { onPopupClicked?.invoke() }
    }

    private fun updateTimeIndexFromX(x: Float): Boolean {
        val t = chartsData.pointerTimeIndex

        chart?.updateTimeIndexFromX(x)

        if (t != chartsData.pointerTimeIndex) {
            pointerPopup?.updatePoints(measuredWidth)
            tint?.updatePoints()
            invalidate()
        }
        return t == chartsData.pointerTimeIndex
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}