package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Color
import au.sjowl.lib.view.charts.telegram.fragment.charts.Themes
import au.sjowl.lib.view.charts.telegram.other.SharedPrefsDelegate

class ChartColors(
    context: Context
) {

    val window: Int

    val background: Int

    val toolbar: Int

    val textToolbar: Int

    val pointer: Int

    val gridLines: Int

    val scrollSelector: Int

    val scrollBackground: Int

    val chartText: Int

    val text: Int

    val chartTitle: Int

    val moonTint: Int

    val colorKnob: Int = Color.WHITE

    val zoomOut: Int

    val pointerShadow = Color.parseColor("#33000000")

    private val theme by SharedPrefsDelegate(context, Themes.KEY_THEME, Themes.LIGHT)

    init {
        when (theme) {
            Themes.LIGHT -> {
                window = Color.parseColor("#f0f0f0")
                background = Color.parseColor("#ffffff")
                toolbar = Color.parseColor("#FFFFFF")
                textToolbar = Color.parseColor("#000000")
                pointer = Color.parseColor("#ffffff")
                gridLines = Color.parseColor("#182D3B")
                scrollSelector = Color.parseColor("#8086A9C4")
                scrollBackground = Color.parseColor("#99E2EEF9")
                chartText = Color.parseColor("#8E8E93")
                text = Color.parseColor("#222222")
                chartTitle = Color.parseColor("#000000")
                moonTint = Color.parseColor("#8f8f94")
                zoomOut = Color.parseColor("#108BE3")
            }
            else -> {
                window = Color.parseColor("#1b2433")
                background = Color.parseColor("#242f3e")
                toolbar = Color.parseColor("#242f3e")
                textToolbar = Color.parseColor("#FFFFFF")
                pointer = Color.parseColor("#1c2533")
                gridLines = Color.parseColor("#FFFFFF")
                scrollSelector = Color.parseColor("#56626D")
                scrollBackground = Color.parseColor("#99304259")
                chartText = Color.parseColor("#99A3B1C2")
                text = Color.parseColor("#e5eff5")
                chartTitle = Color.parseColor("#ffffff")
                moonTint = Color.parseColor("#ffffff")
                zoomOut = Color.parseColor("#48AAF0")
            }
        }
    }
}