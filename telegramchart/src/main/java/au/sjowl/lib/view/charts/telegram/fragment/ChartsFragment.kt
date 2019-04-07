package au.sjowl.lib.view.charts.telegram.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.children
import au.sjowl.lib.view.charts.telegram.ChartContainer
import au.sjowl.lib.view.charts.telegram.R
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes
import au.sjowl.lib.view.charts.telegram.getProperty
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import au.sjowl.lib.view.charts.telegram.setProperty
import kotlinx.android.synthetic.main.fr_charts.*
import kotlinx.android.synthetic.main.rv_item_chart.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

class ChartsFragment : BaseFragment() {

    override val layoutId: Int get() = R.layout.fr_charts

    private val dataFile = "chart_data.json"

    private var theme: Int
        get() = context!!.getProperty(Themes.KEY_THEME, Themes.LIGHT)
        set(value) = context!!.setProperty(Themes.KEY_THEME, value)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()

        getData().forEach { chartData ->
            chartData.initTimeWindow()
            val v = LayoutInflater.from(context).inflate(R.layout.rv_item_chart, chartsContainer, false)
            v.chartContainer.updateTheme()
            v.chartContainer.chartData = chartData
            chartsContainer.addView(v)
        }

        menuTheme.onClick {
            theme = Themes.toggleTheme(theme)
            setTheme()
        }
    }

    private fun setTheme() {
        val colors = ChartColors(context!!)
        chartsContainer.children.forEach { (it as ChartContainer).updateTheme() }
        toolbar.backgroundColor = colors.colorToolbar
        root.backgroundColor = colors.colorBackground
        title.textColor = colors.colorTextToolbar
        menuTheme.imageTintList = ColorStateList.valueOf(colors.colorMoonTint)
        this@ChartsFragment.view?.invalidate()
    }

    private fun getData(): List<ChartData> {
        val json = ResourcesUtils.getResourceAsString(dataFile)
        return ChartColumnJsonParser(json).parse()
    }
}