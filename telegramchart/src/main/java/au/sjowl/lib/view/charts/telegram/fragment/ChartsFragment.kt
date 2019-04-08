package au.sjowl.lib.view.charts.telegram.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.children
import au.sjowl.lib.view.charts.telegram.DateFormatter
import au.sjowl.lib.view.charts.telegram.R
import au.sjowl.lib.view.charts.telegram.TelegramChartView
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes
import au.sjowl.lib.view.charts.telegram.getProperty
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.setProperty
import kotlinx.android.synthetic.main.fr_charts.*
import kotlinx.android.synthetic.main.rv_item_chart.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import kotlin.concurrent.thread

class ChartsFragment : BaseFragment() {

    override val layoutId: Int get() = R.layout.fr_charts

    private var theme: Int
        get() = context!!.getProperty(Themes.KEY_THEME, Themes.LIGHT)
        set(value) = context!!.setProperty(Themes.KEY_THEME, value)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        testRestChartsData()
        setup()

        menuTheme.onClick {
            theme = Themes.toggleTheme(theme)
            setTheme()
        }

        setTheme()
    }

    private fun setup() {
        (1..5).forEach { i ->
            val chartData = getData("contest/$i/overview.json").apply {
                canBeZoomed = true
            }
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            v.chartContainer.updateTheme()
            chartsContainer.addView(v)
            v.chartContainer.onZoomListener = { chartsData, zoomIn ->
                println("zoom $zoomIn")

                if (zoomIn) {
                    val yearMonth = DateFormatter.formatYMShort(chartData.pointerTime)
                    val day = DateFormatter.formatDShort(chartData.pointerTime)
                    v.chartContainer.chartsData = getData("contest/$i/$yearMonth/$day.json").apply {
                        copyStatesFrom(v.chartContainer.chartsData)
                        canBeZoomed = false
                    }
                } else {
                    v.chartContainer.chartsData = getData("contest/$i/overview.json").apply {
                        copyStatesFrom(v.chartContainer.chartsData)
                        canBeZoomed = true
                    }
                }
            }
            v.chartContainer.chartsData = chartData
        }
    }

    private fun testRestChartsData() {
        chartsContainer.removeAllViews()
        (1..5).forEach { i ->
            val chartData = getData("contest/$i/overview.json")
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            chartsContainer.addView(v)
            v.chartContainer.chartsData = chartData
        }

        thread {
            runBlocking {
                delay(1500)
                launch(Dispatchers.Main) {
                    (1..5).forEach { i ->
                        chartsContainer.getChildAt(i - 1).chartContainer.chartsData = getData("contest/2/overview.json")
                    }
                }
            }
        }
    }

    private fun setTheme() {
        val colors = ChartColors(context!!)
        chartsContainer.children.forEach { (it as TelegramChartView).updateTheme() }
        toolbar.backgroundColor = colors.toolbar
        root.backgroundColor = colors.window
        title.textColor = colors.textToolbar
        menuTheme.imageTintList = ColorStateList.valueOf(colors.moonTint)
        this@ChartsFragment.view?.invalidate()
    }

    private fun getData(dataFile: String): ChartsData {
        val json = ResourcesUtils.getResourceAsString(dataFile)
        return ChartColumnJsonParser(json).parseChart()
    }
}