package au.sjowl.lib.view.charts.telegram

import android.content.Context
import android.util.AttributeSet

class TelegramLinearChartView : TelegramChartView {
    override val layoutId: Int get() = R.layout.chart_linear

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}