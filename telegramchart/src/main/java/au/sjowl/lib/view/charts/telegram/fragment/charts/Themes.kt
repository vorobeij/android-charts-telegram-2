package au.sjowl.lib.view.charts.telegram.fragment.charts

object Themes {

    const val LIGHT = 0

    const val DARK = 1

    const val KEY_THEME = "THEME"

    fun toggleTheme(theme: Int): Int {
        return (theme + 1) % 2
    }
}