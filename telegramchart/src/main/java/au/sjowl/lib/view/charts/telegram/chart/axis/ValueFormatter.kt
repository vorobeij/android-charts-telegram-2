package au.sjowl.lib.view.charts.telegram.chart.axis

import java.text.DecimalFormat

class ValueFormatter {

    private val dec0Formatter = DecimalFormat("0")

    private val dec1Formatter = DecimalFormat("0.0")

    fun stepFromRange(min: Int, max: Int, marksSize: Int): Int {
        val interval = max - min

        var i = 0
        var stop = false
        var s = 0

        while (!stop) {
            s = stepFromIndex(i)
            val t = interval / s

            for (j in 1..marksSize * 2) {
                if (t in 0..marksSize * j) {
                    s *= j
                    stop = true
                    break
                }
            }
            i++
        }
        return s
    }

    fun rawMarksFromRange(min: Int, max: Int, intervalsSize: Int): ArrayList<Int> {
        val step = (max - min) / (intervalsSize)
        val list = arrayListOf<Int>()
        for (i in 0..intervalsSize) list.add(step * i + min)
        return list
    }

    fun adjustRange(min: Int, max: Int, intervalsSize: Int): NewRange {
        val marks = marksFromRange(min, max, intervalsSize)
        return NewRange(marks[0], marks[intervalsSize])
    }

    fun format(value: Int): String {
        return when (value) {
            in 0..999 -> value.toString()
            in 1000..10_000 -> {
                val s = value / 100
                if (s % 10 == 0) "${dec0Formatter.format(s * 0.1f)}k"
                else "${dec1Formatter.format(s * 0.1f)}k"
            }
            in 10_000..999_999 -> "${value / 1000}k"
            in 1_000_000..999_999_999 -> "${value / 1_000_000}M"
            else -> value.toString()
        }
    }

    fun marksFromRange(min: Int, max: Int, intervalsSize: Int): ArrayList<Int> {
        val step = stepFromRange(min, max, intervalsSize)
        val minAdjusted = min - min % step
        val maxAdjusted = if (max % step == 0) max else max - (max + step) % step + step
        val stepAdjusted = stepFromRange(minAdjusted, maxAdjusted, intervalsSize)

        val list = arrayListOf<Int>()
        for (i in 0..intervalsSize) list.add(stepAdjusted * i + minAdjusted)

        return list
    }

    private fun stepFromIndex(index: Int): Int {
        return if (index == 0) {
            1
        } else {
            val i = index - 1
            (i + 1) % 2 * 5 * Math.pow(10.0, (i / 2).toDouble()).toInt() + (i) % 2 * 10 * Math.pow(10.0, (i / 2).toDouble()).toInt()
        }
    }

    data class NewRange(
        val min: Int,
        val max: Int
    )
}