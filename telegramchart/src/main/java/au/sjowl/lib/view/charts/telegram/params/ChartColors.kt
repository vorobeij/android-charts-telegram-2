package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Color
import au.sjowl.lib.view.charts.telegram.SharedPrefsDelegate
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes

class ChartColors(
    context: Context
) {

    val window: Int

    val background: Int

    val toolbar: Int

    val textToolbar: Int

    val pointer: Int

    val grid: Int

    val overviewWindow: Int

    val overviewTint: Int

    val chartText: Int

    val text: Int

    val chartTitle: Int

    val moonTint: Int

    private val theme by SharedPrefsDelegate(context, Themes.KEY_THEME, Themes.LIGHT)

    init {
        when (theme) {
            Themes.LIGHT -> {
                window = Color.parseColor("#f0f0f0")
                background = Color.parseColor("#ffffff")
                toolbar = Color.parseColor("#FFFFFF")
                textToolbar = Color.parseColor("#000000")
                pointer = Color.parseColor("#ffffff")
                grid = Color.parseColor("#e7e8e9")
                overviewWindow = Color.parseColor("#8074a0c4")
                overviewTint = Color.parseColor("#9EDEF0FF")
                chartText = Color.parseColor("#9ba6ae")
                text = Color.parseColor("#222222")
                chartTitle = Color.parseColor("#000000")
                moonTint = Color.parseColor("#8f8f94")
            }
            else -> {
                window = Color.parseColor("#1b2433")
                background = Color.parseColor("#242f3e")
                toolbar = Color.parseColor("#242f3e")
                textToolbar = Color.parseColor("#FFFFFF")
                pointer = Color.parseColor("#1c2533")
                grid = Color.parseColor("#353f4d")
                overviewWindow = Color.parseColor("#E0597088")
                overviewTint = Color.parseColor("#912B3A4D")
                chartText = Color.parseColor("#8e8e93")
                text = Color.parseColor("#e5eff5")
                chartTitle = Color.parseColor("#ffffff")
                moonTint = Color.parseColor("#ffffff")
            }
        }
    }
}