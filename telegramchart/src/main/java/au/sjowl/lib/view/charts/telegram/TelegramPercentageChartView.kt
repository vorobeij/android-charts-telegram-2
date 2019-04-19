package au.sjowl.lib.view.charts.telegram

import android.content.Context
import android.util.AttributeSet

class TelegramPercentageChartView : TelegramChartView {
    override val layoutId: Int get() = R.layout.chart_percentage

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}