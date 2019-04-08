package au.sjowl.lib.view.charts.telegram.fragment

import au.sjowl.lib.view.charts.telegram.data.ChartTypes
import org.amshove.kluent.shouldBe
import org.junit.Test

class ChartColumnJsonParserTest {
    @Test
    fun parse() {
        var json = ResourcesUtils.getResourceAsString("contest/1/overview.json")
        var chartsData = ChartColumnJsonParser(json).parseChart()
        with(chartsData) {
            isYScaled shouldBe false
            isStacked shouldBe false
            isPercentage shouldBe false
            columns.size shouldBe 2
            assert(type == ChartTypes.LINE)
        }

        json = ResourcesUtils.getResourceAsString("contest/2/overview.json")
        chartsData = ChartColumnJsonParser(json).parseChart()
        with(chartsData) {
            isYScaled shouldBe true
            isStacked shouldBe false
            isPercentage shouldBe false
            columns.size shouldBe 2
            assert(type == ChartTypes.LINE)
        }

        json = ResourcesUtils.getResourceAsString("contest/3/overview.json")
        chartsData = ChartColumnJsonParser(json).parseChart()
        with(chartsData) {
            isYScaled shouldBe false
            isStacked shouldBe true
            isPercentage shouldBe false
            columns.size shouldBe 7
            assert(type == ChartTypes.BAR)
        }

        json = ResourcesUtils.getResourceAsString("contest/4/overview.json")
        chartsData = ChartColumnJsonParser(json).parseChart()
        with(chartsData) {
            isYScaled shouldBe false
            isStacked shouldBe false
            isPercentage shouldBe false
            columns.size shouldBe 1
            assert(type == ChartTypes.BAR)
        }
        json = ResourcesUtils.getResourceAsString("contest/5/overview.json")
        chartsData = ChartColumnJsonParser(json).parseChart()
        with(chartsData) {
            isYScaled shouldBe false
            isStacked shouldBe true
            isPercentage shouldBe true
            columns.size shouldBe 6
            assert(type == ChartTypes.AREA)
        }
    }
}