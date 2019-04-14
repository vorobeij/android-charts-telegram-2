package au.sjowl.lib.view.charts.telegram.time

import au.sjowl.lib.view.charts.telegram.chart.base.ChartPointerPopup
import au.sjowl.lib.view.charts.telegram.other.measureSpeedMs
import org.junit.Test

class TimeFormatterTest {
    private val slowformatter = ChartPointerPopup.DayFormatter()
    @Test
    fun speedFormattingTest() {
        val max = 9_000_000
        // warmup
        var t = 0L
        for (i in 0..max) {
            slowformatter.format(t)
            t += 10
        }

        t = 0
        measureSpeedMs("format old") {
            for (i in 0..max) {
                slowformatter.format(t)
                t += 10
            }
        }
    }

    @Test
    fun divisionTest() {
        val max = 10_000_000
        // warmup
        var t = 0L
        for (i in 0..max) {
            slowformatter.format(t)
            t += 10
        }

        var sum = 0
        measureSpeedMs("shr") {
            for (i in 0..max) {
                sum += i shr 1
            }
        }

        sum = 0
        measureSpeedMs("/") {
            for (i in 0..max) {
                sum += i / 2
            }
        }
    }
}