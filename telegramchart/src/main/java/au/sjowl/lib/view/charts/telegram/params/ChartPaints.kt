package au.sjowl.lib.view.charts.telegram.params

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import org.jetbrains.anko.sp

class ChartPaints(
    context: Context,
    val colors: ChartColors
) {
    val fontFamily: Typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)

    val paintGrid = paint().apply {
        color = colors.gridLines
        style = Paint.Style.STROKE
        strokeWidth = 3f
        strokeCap = Paint.Cap.ROUND
    }

    val paintArrow = paint().apply {
        color = colors.gridLines
        style = Paint.Style.STROKE
        strokeWidth = 6f
        alpha = 25
        strokeCap = Paint.Cap.ROUND
    }

    val paintChartText = paint().apply {
        color = colors.chartText
        textSize = context.sp(12f) * 1f
    }

    val paintChartLine = paint().apply {
        strokeWidth = 6f
        style = Paint.Style.STROKE
    }

    val paintPointerCircle = paint().apply {
        style = Paint.Style.FILL
        color = colors.background
    }

    val paintOverviewLine = paint().apply {
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    val paintOverviewWindowVerticals = paint().apply {
        style = Paint.Style.FILL
        color = colors.scrollSelector
    }

    val paintOverviewWindowKnob = paint().apply {
        style = Paint.Style.FILL
        color = colors.colorKnob
    }

    val paintOverviewWindowHorizontals = paint().apply {
        strokeWidth = 5f
        style = Paint.Style.STROKE
        color = colors.scrollSelector
    }

    val paintOverviewWindowTint = paint().apply {
        style = Paint.Style.FILL
        color = colors.scrollBackground
    }

    val paintPointerBackground = paint().apply {
        color = colors.pointer
        setShadowLayer(5f, 0f, 2f, Color.parseColor("#33000000"))
    }

    val paintMarksBackground = paint().apply {
        color = colors.background
    }

    val paintPointerTitle = paint().apply {
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        color = colors.text
        textSize = context.sp(12).toFloat()
    }

    val paintPointerValue = paint().apply {
        typeface = fontFamily
        textSize = context.sp(14).toFloat()
    }

    val paintPointerName = paint().apply {
        textSize = context.sp(14).toFloat()
    }

    private fun paint() = Paint().apply { isAntiAlias = true }
}