package au.sjowl.lib.view.charts.telegram

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.forEach
import au.sjowl.lib.view.charts.telegram.chart.container.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.names.ChartItem
import au.sjowl.lib.view.charts.telegram.names.RoundTitledCheckbox
import au.sjowl.lib.view.charts.telegram.other.DateFormatter
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.ValueAnimatorWrapper
import au.sjowl.lib.view.charts.telegram.other.tint
import au.sjowl.lib.view.charts.telegram.overview.ChartOverviewView
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.params.ChartConfig
import au.sjowl.lib.view.charts.telegram.params.ChartDimensions
import kotlinx.android.synthetic.main.chart_linear.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.margin
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

/**
 * Base class for all Telegram charts
 */
open class TelegramChartView : LinearLayout {

    var chartsData: ChartsData = ChartsData()
        set(value) {
            field = value
            value.initTimeWindow()
            value.columns.values.forEach { it.calculateExtremums() }
            titleTextView.text = value.title
            setTimeIntervalTitle()
            setChartNames()
            chartOverview.chartsData = chartsData
            chartContainer.chartsData = chartsData
            axisTime.chartsData = chartsData
            setZoomMode()
        }

    /**
     * zoomIn: true if zoom in, false if zoom out
     */
    var onZoomListener: ((chartsData: ChartsData, zoomIn: Boolean) -> Unit)? = null

    open val layoutId: Int = 0

    private lateinit var chartContainer: BaseChartContainer

    private lateinit var chartOverview: ChartOverviewView

    private var colors = ChartColors(context)

    private val animator = ValueAnimatorWrapper(this::onAnimate)

    private val onChartNameClick = { chartItem: ChartItem, checked: Boolean ->
        onAnimate {
            chartsData.columns[chartItem.chartId]!!.enabled = checked
            chartContainer.onChartStateChanged()
        }
    }

    private val onChartNameLongClick = { chartItem: ChartItem ->
        this@TelegramChartView.chartNames.children.forEach { (it as RoundTitledCheckbox).checked = it.chart!!.chartId == chartItem.chartId }
        onAnimate {
            chartsData.columns.values.forEach { it.enabled = it.id == chartItem.chartId }
            chartContainer.onChartStateChanged()
        }
    }

    private val dimensions = ChartDimensions(context)

    fun updateTheme() {
        colors = ChartColors(context)
        titleTextView.textColor = colors.chartTitle
        timeIntervalTextView.textColor = colors.chartTitle
        chartContainer.updateTheme()
        axisTime.updateTheme()
        chartOverview.updateTheme()
        chartRoot.backgroundColor = colors.background
        chartNames.forEach { (it as ThemedView).updateTheme() }
        zoomOutTextView.tint(colors.zoomOut)
    }

    private fun setZoomMode() {
        var animateTo = if (chartsData.isZoomed) 1f else 0f
        zoomOutTextView.animate()
            .alpha(animateTo)
            .scaleX(animateTo)
            .scaleY(animateTo)
            .setInterpolator(ChartConfig.interpolator())
            .setDuration(ChartConfig.animDuration)
        animateTo = if (chartsData.isZoomed) 0f else 1f
        titleTextView.animate()
            .alpha(animateTo)
            .scaleX(animateTo)
            .scaleY(animateTo)
            .setInterpolator(ChartConfig.interpolator())
            .setDuration(ChartConfig.animDuration)
    }

    private fun onAnimate(v: Float) {
        chartOverview.onAnimateValues(v)
    }

    private fun setChartNames() {
        chartNames.removeAllViews()
        chartsData.columns.values.forEach {
            chartNames.addView(RoundTitledCheckbox(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(wrapContent, wrapContent).apply {
                    margin = dimensions.checkboxMargin
                }
                bind(
                    ChartItem(it.id, it.name, it.color, it.enabled),
                    onChartNameClick,
                    onChartNameLongClick
                )
            })
        }
    }

    private fun setTimeIntervalTitle() {
        timeIntervalTextView.text = DateFormatter.intervalFormat(chartsData.timeStart, chartsData.timeEnd)
    }

    private fun onAnimate(block: () -> Unit) {
        chartOverview.updateStartPoints()
        block.invoke()
        chartOverview.updateFinishState()
        animator.start()
    }

    private fun init(context: Context) {
        context.layoutInflater.inflate(layoutId, this)
        chartContainer = findViewById(R.id.chartViewContainerX)
        chartOverview = findViewById(R.id.chartOverviewX)

        chartOverview.onTimeIntervalChanged = {
            setTimeIntervalTitle()
            chartContainer.onTimeIntervalChanged()
            axisTime.onTimeIntervalChanged()
        }
        chartContainer.onPopupClicked = { onZoomListener?.invoke(chartsData, true) }
        zoomOutTextView.onClick { onZoomListener?.invoke(chartsData, false) }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
}