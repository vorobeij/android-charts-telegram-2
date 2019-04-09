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

    private val range = (2..2)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        testRestChartsData()
        setup()
//        testSwitch()

        menuTheme.onClick {
            theme = Themes.toggleTheme(theme)
            setTheme()
        }

        setTheme()
    }

    private fun setup() {
        range.forEach { i ->
            val newChartsData = getChartsData("contest/$i/overview.json").apply {
                canBeZoomed = true
            }
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            v.chartContainer.updateTheme()
            chartsContainer.addView(v)
            v.chartContainer.onZoomListener = { chartsData, zoomIn ->

                try {
                    if (zoomIn && v.chartContainer.chartsData.canBeZoomed) {
                        val yearMonth = DateFormatter.formatYMShort(chartsData.pointerTime)
                        val day = DateFormatter.formatDShort(chartsData.pointerTime)
                        val jsonStr = "contest/$i/$yearMonth/$day.json"
                        v.chartContainer.chartsData = getChartsData(jsonStr).apply {
                            canBeZoomed = false
                            copyStatesFrom(v.chartContainer.chartsData)
                        }
                    } else if (!zoomIn && !v.chartContainer.chartsData.canBeZoomed) {
                        v.chartContainer.chartsData = getChartsData("contest/$i/overview.json").apply {
                            canBeZoomed = true
                            copyStatesFrom(v.chartContainer.chartsData)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            v.chartContainer.chartsData = newChartsData
        }
    }

    private fun testRestChartsData() {
        chartsContainer.removeAllViews()
        range.forEach { i ->
            val chartData = getChartsData("contest/$i/overview.json")
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            chartsContainer.addView(v)
            v.chartContainer.chartsData = chartData
        }

        thread {
            runBlocking {
                delay(1000)
                launch(Dispatchers.Main) {
                    range.forEach { i ->
                        chartsContainer.getChildAt(i - 1).chartContainer.chartsData = getChartsData("contest/2/overview.json")
                    }
                }
            }
        }
    }

    private fun testSwitch() {
        (1..1).forEach { i ->
            val chartData = getChartsData("contest/$i/overview.json")
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            chartsContainer.addView(v)
            v.chartContainer.chartsData = chartData

            v.chartContainer.onZoomListener = { chartsData, zoomIn ->
                println("zoom $zoomIn")

                try {
                    if (zoomIn && v.chartContainer.chartsData.canBeZoomed) {
                        v.chartContainer.chartsData = getChartsData("contest/1/overview.json").apply {
                            copyStatesFrom(v.chartContainer.chartsData)
                            canBeZoomed = false
                        }
                    } else if (!zoomIn && !v.chartContainer.chartsData.canBeZoomed) {
                        v.chartContainer.chartsData = getChartsData("contest/2/overview.json").apply {
                            copyStatesFrom(v.chartContainer.chartsData)
                            canBeZoomed = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread {
            runBlocking {
                delay(1000)
                launch(Dispatchers.Main) {
                    chartsContainer.getChildAt(0).chartContainer.chartsData = getChartsData("contest/2/overview.json")
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

    private fun getChartsData(dataFile: String): ChartsData {
        val json = ResourcesUtils.getResourceAsString(dataFile)
        return ChartColumnJsonParser(json).parseChart()
    }
}