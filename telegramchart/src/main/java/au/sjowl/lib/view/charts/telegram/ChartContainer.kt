package au.sjowl.lib.view.charts.telegram

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.forEach
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.names.ChartItem
import au.sjowl.lib.view.charts.telegram.names.RoundTitledCheckbox
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import kotlinx.android.synthetic.main.chart_layout.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.dip
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.margin
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

class ChartContainer : LinearLayout {

    var chartData: ChartData = ChartData()
        set(value) {
            field = value
            value.columns.values.forEach { it.calculateExtremums() }
            titleTextView.text = value.title

            chartOverview.chartData = chartData
            chartView.chartData = chartData
            axisTime.chartData = chartData

            updateTimeIntervalTitle()
            chartNames.removeAllViews()
            value.columns.values.forEach {
                chartNames.addView(RoundTitledCheckbox(context).apply {
                    bind(
                        ChartItem(it.id, it.name, it.color, it.enabled),
                        { chartItem, checked ->
                            onAnimate(floatValueAnimator) {
                                chartData.columns[chartItem.chartId]!!.enabled = checked
                            }
                        },
                        { chartItem ->
                            this@ChartContainer.chartNames.children.forEach { (it as RoundTitledCheckbox).checked = it.chart!!.chartId == chartItem.chartId }
                            onAnimate(floatValueAnimator) {
                                chartData.columns.values.forEach { it.enabled = it.id == chartItem.chartId }
                            }
                        })
                    layoutParams = ViewGroup.MarginLayoutParams(wrapContent, wrapContent).apply {
                        margin = context.dip(5)
                    }
                })
            }

            requestLayout()
        }

    private var colors = ChartColors(context)

    private var animValue = 1f

    private val floatValueAnimator = ValueAnimator().apply {
        setFloatValues(1f, 0f)
        duration = 120
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            val v = animatedValue as Float
            if (v != animValue) {
                animValue = v
                chartOverview.onAnimateValues(v)
                chartView.onAnimateValues(v)
            }
        }
    }

    override fun onDetachedFromWindow() {
        floatValueAnimator.cancel()
        floatValueAnimator.removeAllUpdateListeners()
        super.onDetachedFromWindow()
    }

    fun updateTheme() {
        colors = ChartColors(context)
        titleTextView.textColor = colors.chartTitle
        timeIntervalTextView.textColor = colors.chartTitle
        chartView.updateTheme(colors)
        axisTime.updateTheme(colors)
        chartOverview.updateTheme(colors)
        chartRoot.backgroundColor = colors.background
        chartNames.forEach { (it as ThemedView).updateTheme(colors) }
    }

    private fun updateTimeIntervalTitle() {
        timeIntervalTextView.text = DateFormatter.intervalFormat(chartData.timeStart, chartData.timeEnd)
    }

    private fun onAnimate(animator: ValueAnimator, block: () -> Unit) {
        chartOverview.updateStartPoints()
        chartView.updateStartPoints()

        block.invoke()

        chartOverview.updateFinishState()
        chartView.updateFinishState()

        animator.start()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        context.layoutInflater.inflate(R.layout.chart_layout, this)
        chartOverview.onTimeIntervalChanged = {
            updateTimeIntervalTitle()
            chartView.onTimeIntervalChanged()
            axisTime.onTimeIntervalChanged()
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }
}