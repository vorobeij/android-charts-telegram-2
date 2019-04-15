package au.sjowl.lib.view.charts.telegram.other

import org.junit.Test
import kotlin.system.measureNanoTime

class DateFormatterTest {
    @Test
    fun formatterTest() {
        val max = 9_000_000
        // warmup
        var t = 0L
        for (i in 0..max) {
            DateFormatter.intervalFormat(t, t + 20_000)
            t += 10
        }

        val t1 = measureNanoTime {
            var t = 0L
            for (i in 0..max) {
                DateFormatter.intervalFormat(t, t + 20_000)
                t += 100
            }
        }
        println("format time %.6f".format(t1 / 1000000f / max))
    }
}