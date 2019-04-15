package au.sjowl.lib.view.chart

import au.sjowl.lib.view.charts.telegram.chart.base.axis.ValueFormatter
import au.sjowl.lib.view.charts.telegram.other.measureSpeedMs
import org.amshove.kluent.shouldBe
import org.junit.Test
import kotlin.test.assertEquals

class ValueFormatterTest {
    val formatter = ValueFormatter()

    @Test
    fun marksFromRangeTest() {
        assertEquals(formatter.marksFromRange(1, 250, 5), arrayListOf(0, 50, 100, 150, 200, 250))
        assertEquals(formatter.marksFromRange(1, 252, 5), arrayListOf(0, 60, 120, 180, 240, 300))
        assertEquals(formatter.marksFromRange(1, 50, 5), arrayListOf(0, 10, 20, 30, 40, 50))
        assertEquals(formatter.marksFromRange(1, 100, 5), arrayListOf(0, 20, 40, 60, 80, 100))
        assertEquals(formatter.marksFromRange(1, 105, 5), arrayListOf(0, 25, 50, 75, 100, 125))
        assertEquals(formatter.marksFromRange(1, 102, 5), arrayListOf(0, 25, 50, 75, 100, 125))
        assertEquals(formatter.marksFromRange(1, 110, 5), arrayListOf(0, 25, 50, 75, 100, 125))
        assertEquals(formatter.marksFromRange(5, 99, 5), arrayListOf(0, 20, 40, 60, 80, 100))
        assertEquals(formatter.marksFromRange(26, 278, 5), arrayListOf(0, 60, 120, 180, 240, 300))
    }

    @Test
    fun stepFromRangeTest() {
        formatter.stepFromRange(1, 250, 5) shouldBe 50
        formatter.stepFromRange(10, 250, 5) shouldBe 50
        formatter.stepFromRange(10, 230, 5) shouldBe 50
        formatter.stepFromRange(5, 99, 5) shouldBe 20
        formatter.stepFromRange(5, 10, 5) shouldBe 1
        formatter.stepFromRange(5, 200, 5) shouldBe 40
    }

    @Test
    fun formatRangeTest() {
        assertEquals("5", formatter.format(5))
        assertEquals("403", formatter.format(403))
        assertEquals("1.2k", formatter.format(1200))
        assertEquals("1k", formatter.format(1000))
        assertEquals("2.3k", formatter.format(2300))
        assertEquals("10k", formatter.format(10_000))
        assertEquals("23k", formatter.format(23001))
        assertEquals("2M", formatter.format(2100000))
        assertEquals("21M", formatter.format(21000000))
        assertEquals("21M", formatter.format(21000001))
        assertEquals("21M", formatter.format(21400001))
    }

    @Test
    fun speedFormatTest() {
        val max = 10_000_000
        // warmup
        for (i in 0..max) {
            formatter.format(i)
        }

        measureSpeedMs("stepFromRange:") {
            for (i in 0..max) {
                formatter.stepFromRange(0, i, 5)
            }
        }
    }

    @Test
    fun operatorsTest() {
        val s = 100 + 100 shl 1
        println(s)
    }
}