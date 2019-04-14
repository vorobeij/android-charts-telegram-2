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

    val zoomOutText: Int

    val pointerShadow = Color.parseColor("#33000000")

    val checkboxTitle = Color.parseColor("#ffffff")

    val tooltipArrow: Int

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
                zoomOutText = Color.parseColor("#108BE3")
                tooltipArrow = Color.parseColor("#D2D5D7")
            }
            else -> {
                window = Color.parseColor("#1b2433")
                background = Color.parseColor("#242f3e")
                toolbar = Color.parseColor("#242f3e")
                textToolbar = Color.parseColor("#FFFFFF")
                pointer = Color.parseColor("#1c2533")
                gridLines = Color.parseColor("#FFFFFF")
                scrollSelector = Color.parseColor("#806F899E")
                scrollBackground = Color.parseColor("#99304259")
                chartText = Color.parseColor("#99A3B1C2")
                text = Color.parseColor("#e5eff5")
                chartTitle = Color.parseColor("#ffffff")
                moonTint = Color.parseColor("#ffffff")
                zoomOutText = Color.parseColor("#48AAF0")
                tooltipArrow = Color.parseColor("#D2D5D7")
            }
        }
    }
}
/*
Day Theme
Scroll Background E2EEF9, 60%

Scroll Selector (overlays the value above) 86A9C4, 50%

Grid Lines 182D3B, 10%

Zoom Out Text 108BE3

Tooltip Arrow D2D5D7


Followers, Interactions, Growth
Red Line FE3C30, Button E65850, Tooltip Text F34C44

Green Line 4BD964, Button 5FB641, Tooltip Text 3CC23F

Blue Line 108BE3, Button 3497ED, Tooltip Text 108BE3

Yellow Line E8AF14, Button F5BD25, Tooltip Text E4AE1B

X/Y Axis Text 8E8E93



Messages, Apps
Bars and Charts:

Blue 3497ED, Dark Blue 2373DB, Light Green 9ED448, Green 5FB641, Yellow F5BD25, Orange F79E39, Red E65850, Light Blue 55BFE6

Buttons:

Blue 3497ED, Dark Blue 3381E8, Light Green 9ED448, Green 5FB641, Yellow F5BD25, Orange F79E39, Red E65850, Light Blue 35AADC

Text:

Blue 108BE3, Dark Blue 2373DB, Light Green 89C32E, Green 4BAB29, Yellow EAAF10, Orange F58608, Red F34C44, Light Blue 269ED4

X/Y Axis Text 252529, 50%

Lighten Mask FFFFFF, 50%



Onlines
Blue Line 64ADED, Button 3896E8, Tooltip Text 3896E8

Dark Blue Line 558DED, Button 558DED, Tooltip Text 558DED

Light Blue Line 5CBCDF, Button 5CBCDF, Tooltip Text 5CBCDF



Night Theme
Tooltip Arrow D2D5D7

Grid Lines FFFFFF, 10%

Zoom Out Text 48AAF0

Scroll Background 304259, 60%

Scroll Selector (overlays the value above) 6F899E, 50%


Followers, Interactions, Growth
Red Line E6574F, Button CF5D57, Tooltip Text F7655E

Green Line 4BD964, Button 5AB34D, Tooltip Text 4BD964

Blue Line 108BE3, Button 4681BB, Tooltip Text 108BE3

Yellow Line DEB93F, Button C9AF4F, Tooltip Text DEB93F

X/Y Axis Text A3B1C2, 60%





Messages, Apps
Bars and Charts:

Blue 4681BB, Dark Blue 345B9C, Light Green 88BA52, Green 3DA05A, Yellow D9B856, Orange D49548, Red CF5D57, Light Blue 479FC4

Buttons:

Blue 4681BB, Dark Blue 466FB3, Light Green 88BA52, Green 3DA05A, Yellow F5BD25, Orange D49548, Red CF5D57, Light Blue 479FC4

Text:

Blue 5199DF, Dark Blue 3E65CF, Light Green 99CF60, Green 3CB560, Yellow DBB630, Orange EE9D39, Red F7655E, Light Blue 43ADDE

Y Axis Text ECF2F8, 50%

X Axis Text A3B1C2, 60%

Darken Mask 242F3E, 50%



Onlines
Blue Line 4082CE, Button 4082CE, Tooltip Text 4082CE

Dark Blue Line 4461AB, Button 4461AB, Tooltip Text 4461AB

Light Blue Line 4697B3, Button 4697B3, Tooltip Text 4697B3
 */