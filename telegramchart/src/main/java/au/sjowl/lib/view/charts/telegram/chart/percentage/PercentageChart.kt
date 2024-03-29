package au.sjowl.lib.view.charts.telegram.chart.percentage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import au.sjowl.lib.view.charts.telegram.chart.linear.LinearChart
import au.sjowl.lib.view.charts.telegram.data.ChartData
import au.sjowl.lib.view.charts.telegram.data.ChartsData
import au.sjowl.lib.view.charts.telegram.params.ChartLayoutParams

open class PercentageChart(
    chartData: ChartData,
    chartsData: ChartsData,
    chartLayoutParams: ChartLayoutParams
) : LinearChart(chartData, chartsData, chartLayoutParams) {

    private val columns = chartsData.charts

    private val chartIndex = chartsData.charts.indexOf(chartData)

    override fun onDraw(canvas: Canvas) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            canvas.save()
            canvas.clipPath(path)
            canvas.drawColor(paints.paintChartLine.color)
            canvas.restore()
        } else {
            canvas.drawPath(path, paints.paintChartLine)
        }
    }

    override fun updateTheme(context: Context) {
        paints = ChartPaints(context).apply {
            paintChartLine.style = Paint.Style.FILL
        }
    }

    override fun drawPointer(canvas: Canvas) = Unit

    override fun y(index: Int) = mh - kY * percentValue(index)

    override fun updateOnAnimation() {
        super.updateOnAnimation()
        updatePathFromPoints()
    }

    private fun updatePathFromPoints() {
        with(path) {
            reset()
            if (drawingPoints.size > 1) {
                val start = innerTimeIndexStart shl 1
                val end = innerTimeIndexEnd shl 1
                moveTo(drawingPoints[start], chartLayoutParams.bottom)
                for (i in start..end step 2) {
                    lineTo(drawingPoints[i], drawingPoints[i + 1])
                }
                lineTo(drawingPoints[end], chartLayoutParams.bottom)
                close()
            }
        }
    }

    private inline fun percentValue(index: Int): Int {
        val sums = chartsData.sums[index]
        if (sums == 0) return 0
        var y0 = 0
        for (k in 0..chartIndex) y0 += if (columns[k].enabled) columns[k].values[index] else 0
        return 100 * y0 / sums
    }
}