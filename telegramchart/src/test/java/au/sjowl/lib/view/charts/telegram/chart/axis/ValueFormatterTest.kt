package au.sjowl.lib.view.charts.telegram.chart.axis

import au.sjowl.lib.view.charts.telegram.chart.base.axis.ValueFormatter
import org.junit.Test

class ValueFormatterTest {
    val v = ValueFormatter()
    @Test
    fun marksFromRangeTest() {
        println(v.rawMarksFromRange(0, 100, 5))
    }

    @Test
    fun adjustRangeTest() {
        println("0, 120 6 should be 0, 120 ->   ${v.adjustRange(0, 120, 6)}")
        println("0, 25214800 6 should be 2100, 4360 ->   ${v.adjustRange(0, 25214800, 5)}")
        println("0, 124260    ->   ${v.adjustRange(0, 124260, 5)}")
        println("2280, 4010   ->   ${v.adjustRange(2280, 4010, 5)}")
        println()
        println("0, 200, 3 should be [0,100, 200]      ->   ${v.rawMarksFromRange(0, 200, 2)}")
        println("0, 300, 4 should be [0,100, 200, 300] ->   ${v.rawMarksFromRange(0, 300, 3)}")
        println("0, 125000                             ->   ${v.rawMarksFromRange(0, 125000, 5)}")
        println("2100, 4350                            ->   ${v.rawMarksFromRange(2100, 4350, 5)}")
    }
}