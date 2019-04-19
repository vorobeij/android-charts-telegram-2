package au.sjowl.lib.view.charts.telegram.chart.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import au.sjowl.lib.view.charts.telegram.chart.base.AbstractChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.other.SLog
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

class PieChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : AbstractChart(chartData, chartsData, chartLayoutParams) {

    val rmax = chartLayoutParams.dimensions.chartPaddingTop * 1f

    private val oval = RectF()

    private val titleRect = Rect()

    private var r = 0f

    private var cx = 0f

    private var cy = 0f

    private val charts = ArrayList<ChartWrapper>().apply {
        chartsData.charts.forEach { add(ChartWrapper(chartsData, it)) }
    }

    private val startAngle = 0f

    override fun onDraw(canvas: Canvas) {
        calcOval() // todo onSizeChanged only

        var angleStart = startAngle
        for (chartWrapper in charts) {
            withOval(angleStart, chartWrapper) { dx, dy ->
                paints.paintChartLine.color = chartWrapper.chart.color
                canvas.drawArc(oval, angleStart, chartWrapper.angle, true, paints.paintChartLine)
            }
            angleStart += chartWrapper.angle
        }

        angleStart = startAngle

        charts.forEach { chartWrapper ->
            if (chartWrapper.chart.enabled) {
                withOval(angleStart, chartWrapper) { dx, dy ->
                    val title = "${(chartWrapper.percent * 100).toInt()}%"
                    paints.title.getTextBounds(title, 0, title.length, titleRect)
                    val a = (angleStart + chartWrapper.angle / 2) * Math.PI / 180.0
                    val x = (cx + dx + r * 0.6 * Math.cos(a)).toFloat() - titleRect.width() / 2
                    val y = (cy + dy + r * 0.6 * Math.sin(a)).toFloat() + titleRect.height() / 2
                    paints.paintChartLine.color = chartWrapper.chart.color
                    canvas.drawText(title, x, y, paints.title)
                }
            }

            angleStart += chartWrapper.angle
        }
    }

    override fun draw(canvas: Canvas) = onDraw(canvas)

    override fun updateTheme(context: Context) {
        paints = PieChartPaints(context)
    }

    override fun fixPointsFrom() {
        charts.forEach { it.fixAngleFrom() }
        chartsData.calcSums()
        charts.forEach { it.fixAngle() }
    }

    override fun updateOnAnimation() {
        charts.forEach { it.interpolateAngle(animValue) }
    }

    override fun calculatePoints() {
        chartsData.calcSums()
        charts.forEach { it.fixAngle() }
    }

    fun intersect(x1: Float, y1: Float): Int {
        calcOval() // todo onSizeChanged only

        val x = x1 - cx
        val y = y1 - cy

        val r0 = Math.sqrt(1.0 * x * x + y * y)
        val alpha = Math.acos(1.0 * x / r) * 180 / Math.PI
        val phi0 = if (y < 0) 360 - alpha else alpha
        SLog.d("phi = $phi0")

        if (r0 <= r) {
            var angleStart = startAngle
            for (i in 0 until charts.size) {
                val chartWrapper = charts[i]
                if (angleStart < phi0 && angleStart + chartWrapper.angle > phi0) {
                    return i
                }
                angleStart += chartWrapper.angle
            }
            return -1
        }
        return -1
    }

    fun select(event: MotionEvent): Boolean {
        val intersected = intersect(event.x, event.y)
        charts.forEach { it.selected = false }
        if (intersected >= 0) {
            SLog.d("Selected ${charts[intersected].chart.name}")
            charts[intersected].selected = true
            return true
        }
        return false
    }

    private fun withOval(angleStart: Float, chartWrapper: ChartWrapper, block: ((dx: Float, dy: Float) -> Unit)) {
        val dx = chartWrapper.dx(angleStart + chartWrapper.angle / 2)
        val dy = chartWrapper.dy(angleStart + chartWrapper.angle / 2)
        SLog.d("dx = $dx, dy = $dy")
        oval.left += dx
        oval.right += dx
        oval.top += dy
        oval.bottom += dy
        block(dx, dy)
        oval.left -= dx
        oval.right -= dx
        oval.top -= dy
        oval.bottom -= dy
    }

    private fun calcOval() {
        oval.top = chartLayoutParams.paddingTop.toFloat()
        oval.bottom = chartLayoutParams.h - chartLayoutParams.paddingTop
        val w = (oval.bottom - oval.top) / 2
        cx = chartLayoutParams.w / 2
        oval.left = cx - w
        oval.right = cx + w
        r = w
        cy = oval.top + w
    }

    class PieChartPaints(context: Context) : ChartPaints(context) {
        override val paintChartLine = antiAliasPaint().apply {
            strokeWidth = dimensions.chartLineWidth
            style = Paint.Style.FILL
        }
    }

    inner class ChartWrapper(
        val charts: ChartsData,
        val chart: ChartData
    ) {

        var selected = false
            set(value) {
                field = value
                selectedTo = if (value) 1 else 0
                dr = rmax * selectedTo
            }

        var angle = 180f
            private set

        var angleFrom = 180f
            private set

        var angleTo = 180f
            private set

        var dr = 0f
            private set

        val percent get() = 1f * chart.sum / charts.enabledChartsSum

        private var selectedTo = 0

        fun dx(angle: Float): Float {
            return (Math.cos(angle * Math.PI / 180) * dr).toFloat()
        }

        fun dy(angle: Float): Float {
            return (Math.sin(angle * Math.PI / 180) * dr).toFloat()
        }

        fun fixAngle() {
            angle = 360f * chart.sum / charts.enabledChartsSum
            fixAngleTo()
        }

        fun fixAngleFrom() {
            angleFrom = angle
        }

        fun fixAngleTo() {
            angleTo = angle
        }

        fun interpolateAngle(v: Float) { // 1->0
            angle = angleTo + (angleFrom - angleTo) * v
        }
    }
}