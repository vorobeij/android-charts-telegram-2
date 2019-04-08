package au.sjowl.lib.view.charts.telegram.fragment

import android.graphics.Color
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import org.json.JSONArray
import org.json.JSONObject

class ChartColumnJsonParser(val json: String) {

    fun parseArray(): List<ChartsData> {

        val charts = JSONArray(json)
        val chartDataList = arrayListOf<ChartsData>()

        for (i in 0 until charts.length()) {
            chartDataList.add(nextChart(charts[i] as JSONObject))
        }

        return chartDataList
    }

    fun parseChart(): ChartsData {
        return nextChart(JSONObject(json))
    }

    private fun nextChart(chart: JSONObject): ChartsData {
        val chartsData = ChartsData()

        val colors = chart.getJSONObject("colors")
        colors.keys().forEach { key ->
            chartsData.columns[key] = ChartData(key).apply {
                color = Color.parseColor(colors.getString(key))
            }
        }

        val names = chart.getJSONObject("names")
        names.keys().forEach { key ->
            chartsData.columns[key]?.name = names.getString(key)
        }

        val types = chart.getJSONObject("types")
        types.keys().forEach { key ->
            chartsData.columns[key]?.run {
                type = types.getString(key)
                chartsData.type = type
            }
        }

        val columns = chart.getJSONArray("columns")
        for (j in 0 until columns.length()) {
            val jsonColumn = columns[j] as JSONArray
            val key = jsonColumn[0] as String

            if (key == "x") {
                for (k in 1 until jsonColumn.length()) {
                    chartsData.time.values.add(jsonColumn[k] as Long)
                }
            } else {
                val column = chartsData.columns[key] as ChartData
                for (k in 1 until jsonColumn.length()) {
                    column.values.add(jsonColumn[k] as Int)
                }
            }
        }

        try {
            chartsData.isPercentage = chart.getBoolean("percentage")
        } catch (e: Exception) {
        }
        try {
            chartsData.isStacked = chart.getBoolean("stacked")
        } catch (e: Exception) {
        }
        try {
            chartsData.isYScaled = chart.getBoolean("y_scaled")
        } catch (e: Exception) {
        }
        return chartsData
    }
}