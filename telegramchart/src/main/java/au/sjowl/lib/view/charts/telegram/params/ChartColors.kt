package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Color
import au.sjowl.lib.view.charts.telegram.SharedPrefsDelegate
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes

class ChartColors(
    context: Context
) {

    val colorWindow: Int

    val colorBackground: Int

    val colorToolbar: Int

    val colorTextToolbar: Int

    val colorPointer: Int

    val colorGrid: Int

    val colorOverviewWindow: Int

    val colorOverviewTint: Int

    val colorChartText: Int

    val colorText: Int

    val colorChartTitle: Int

    val colorMoonTint: Int

    private val theme by SharedPrefsDelegate(context, Themes.KEY_THEME, Themes.LIGHT)

    init {
        when (theme) {
            Themes.LIGHT -> {
                colorWindow = Color.parseColor("#f0f0f0")
                colorBackground = Color.parseColor("#ffffff")
                colorToolbar = Color.parseColor("#FFFFFF")
                colorTextToolbar = Color.parseColor("#000000")
                colorPointer = Color.parseColor("#ffffff")
                colorGrid = Color.parseColor("#e7e8e9")
                colorOverviewWindow = Color.parseColor("#8074a0c4")
                colorOverviewTint = Color.parseColor("#9EDEF0FF")
                colorChartText = Color.parseColor("#9ba6ae")
                colorText = Color.parseColor("#222222")
                colorChartTitle = Color.parseColor("#3c98d5")
                colorMoonTint = Color.parseColor("#8f8f94")
            }
            else -> {
                colorWindow = Color.parseColor("#151e27")
                colorBackground = Color.parseColor("#1d2733")
                colorToolbar = Color.parseColor("#212d3b")
                colorTextToolbar = Color.parseColor("#FFFFFF")
                colorPointer = Color.parseColor("#202b38")
                colorGrid = Color.parseColor("#111924")
                colorOverviewWindow = Color.parseColor("#7E2B4256")
                colorOverviewTint = Color.parseColor("#8019232E")
                colorChartText = Color.parseColor("#506372")
                colorText = Color.parseColor("#e5eff5")
                colorChartTitle = Color.parseColor("#7bc4fb")
                colorMoonTint = Color.parseColor("#ffffff")
            }
        }
    }
}