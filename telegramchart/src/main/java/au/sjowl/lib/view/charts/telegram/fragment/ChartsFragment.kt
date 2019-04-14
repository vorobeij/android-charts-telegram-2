package au.sjowl.lib.view.charts.telegram.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import au.sjowl.lib.view.charts.telegram.R
import au.sjowl.lib.view.charts.telegram.TelegramAreaChartView
import au.sjowl.lib.view.charts.telegram.TelegramChartView
import au.sjowl.lib.view.charts.telegram.TelegramLinearChartView
import au.sjowl.lib.view.charts.telegram.TelegramSingleBarChartView
import au.sjowl.lib.view.charts.telegram.TelegramStackedBarsChartView
import au.sjowl.lib.view.charts.telegram.TelegramYScaledChartView
import au.sjowl.lib.view.charts.telegram.data.ChartTypes
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes
import au.sjowl.lib.view.charts.telegram.other.DateFormatter
import au.sjowl.lib.view.charts.telegram.other.getProperty
import au.sjowl.lib.view.charts.telegram.other.setProperty
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import kotlinx.android.synthetic.main.fr_charts.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

class ChartsFragment : BaseFragment() {

    override val layoutId: Int get() = R.layout.fr_charts

    private var theme: Int
        get() = context!!.getProperty(Themes.KEY_THEME, Themes.LIGHT)
        set(value) = context!!.setProperty(Themes.KEY_THEME, value)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()

        menuTheme.onClick {
            theme = Themes.toggleTheme(theme)
            setTheme()
        }

        setTheme()
    }

    private fun getView(c: ChartsData): TelegramChartView {
        return when (c.type) {
            ChartTypes.LINE -> if (!c.isYScaled) TelegramLinearChartView(context!!) else TelegramYScaledChartView(context!!)
            ChartTypes.BAR -> if (c.isStacked) TelegramStackedBarsChartView(context!!) else TelegramSingleBarChartView(context!!)
            ChartTypes.AREA -> TelegramAreaChartView(context!!)
            else -> throw IllegalStateException("")
        }.apply {
            layoutParams = ViewGroup.MarginLayoutParams(matchParent, wrapContent).apply {
                setMargins(0, context.dip(28), 0, 0)
            }
        }
    }

    private fun setup() {

        val titles = arrayOf(
            1 to "Followers",
            2 to "Interactions",
            3 to "Messages",
            4 to "Views",
            5 to "Apps"
        )

        val titles2 = arrayOf(
            1 to "Followers"
        )

        titles.forEach { pair ->
            val i = pair.first
            val newChartsData = getChartsData("contest/$i/overview.json").apply {
                canBeZoomed = true
                title = pair.second
            }

            val v = getView(newChartsData)
            chartsContainer.addView(v)
            v.updateTheme()
            v.onZoomListener = { chartsData, zoomIn ->

                try {
                    if (zoomIn && v.chartsData.canBeZoomed) {
                        val yearMonth = DateFormatter.formatYMShort(chartsData.pointerTime)
                        val day = DateFormatter.formatDShort(chartsData.pointerTime)
                        val jsonStr = "contest/$i/$yearMonth/$day.json"
                        v.chartsData = getChartsData(jsonStr).apply {
                            canBeZoomed = false
                            copyStatesFrom(v.chartsData)
                        }
                    } else if (!zoomIn && !v.chartsData.canBeZoomed) {
                        v.chartsData = getChartsData("contest/$i/overview.json").apply {
                            canBeZoomed = true
                            copyStatesFrom(v.chartsData)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            v.chartsData = newChartsData
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