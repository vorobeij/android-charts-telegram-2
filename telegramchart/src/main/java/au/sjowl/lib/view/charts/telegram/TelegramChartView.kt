package au.sjowl.lib.view.charts.telegram

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.forEach
import au.sjowl.lib.view.charts.telegram.chart.base.BaseChartContainer
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.names.ChartItem
import au.sjowl.lib.view.charts.telegram.names.RoundTitledCheckbox
import au.sjowl.lib.view.charts.telegram.other.DateFormatter
import au.sjowl.lib.view.charts.telegram.other.ThemedView
import au.sjowl.lib.view.charts.telegram.other.setVisible
import au.sjowl.lib.view.charts.telegram.other.tint
import au.sjowl.lib.view.charts.telegram.overview.base.BaseChartOverviewContainer
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
            onChartsDataChanged()
        }

    /**
     * zoomIn: true if zoom in, false if zoom out
     */
    var onZoomListener: ((chartsData: ChartsData, zoomIn: Boolean) -> Unit)? = null

    open val layoutId: Int = 0

    private var chartContainer: BaseChartContainer? = null

    private var chartOverview: BaseChartOverviewContainer? = null

    private var colors = ChartColors(context)

    private val onChartNameClick = { chartItem: ChartItem, checked: Boolean ->
        chartsData.columns[chartItem.chartId]!!.enabled = checked
        chartContainer?.onChartStateChanged()
        chartOverview?.onChartStateChanged()
        Unit
    }

    private val onChartNameLongClick = { chartItem: ChartItem ->
        chartsData.charts.forEach { it.enabled = it.id == chartItem.chartId }
        this@TelegramChartView.chartNames.children.forEach { (it as RoundTitledCheckbox).check(it.chart!!.chartId == chartItem.chartId, true) }
        chartContainer?.onChartStateChanged()
        chartOverview?.onChartStateChanged()
        Unit
    }

    private val dimensions = ChartDimensions(context)

    open fun onChartsDataChanged() {
        chartsData.initTimeWindow()
        chartsData.calcChartsExtremums()
        titleTextView.text = chartsData.title
        setTimeIntervalTitle()
        setChartNames()
        chartOverview?.chartsData = chartsData
        chartContainer?.chartsData = chartsData
        axisTime.chartsData = chartsData
        setZoomMode()
    }

    fun updateTheme() {
        colors = ChartColors(context)
        titleTextView.textColor = colors.chartTitle
        timeIntervalTextView.textColor = colors.chartTitle
        chartContainer?.updateTheme()
        chartOverview?.updateTheme()
        axisTime.updateTheme()
        chartRoot.backgroundColor = colors.background
        chartNames.forEach { (it as ThemedView).updateTheme() }
        zoomOutTextView.tint(colors.zoomOut)
    }

    protected open fun onZoom(chartsData: ChartsData, zoomIn: Boolean) {
        onZoomListener?.invoke(chartsData, zoomIn)
    }

    protected fun animateContainer(view: View, show: Boolean) {
        val animateTo = if (show) 1f else 0f
        view.animate()
            .alpha(animateTo)
            .scaleX(animateTo)
            .scaleY(animateTo)
            .setInterpolator(ChartConfig.interpolator())
            .setDuration(ChartConfig.animDuration)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    view.setVisible(animateTo == 1f)
                }

                override fun onAnimationCancel(animation: Animator?) {
                    view.setVisible(animateTo == 1f)
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
    }

    private fun setZoomMode() {
        animateContainer(zoomOutTextView, show = chartsData.isZoomed)
        animateContainer(titleTextView, show = !chartsData.isZoomed)
    }

    private fun setChartNames() {
        chartNames.removeAllViews()
        chartsData.charts.forEach {
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

    private fun init(context: Context) {
        context.layoutInflater.inflate(layoutId, this)
        chartContainer = findViewById(R.id.chartViewContainerX)
        chartOverview = findViewById(R.id.chartOverviewX)

        chartOverview?.onTimeIntervalChanged = {
            setTimeIntervalTitle()
            chartContainer?.onTimeIntervalChanged()
            axisTime.onTimeIntervalChanged()
        }
        chartContainer?.onPopupClicked = { onZoom(chartsData, true) }
        zoomOutTextView.onClick { onZoom(chartsData, false) }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }
}