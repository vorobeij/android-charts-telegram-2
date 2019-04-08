package au.sjowl.lib.view.charts.telegram.names

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class ChartNamesContainer : ViewGroup, ThemedView {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEach { it.measure(wrapContent, wrapContent) }

        val maxW = View.getDefaultSize(matchParent, widthMeasureSpec)

        var left = paddingLeft
        var top = paddingTop + if (childCount > 0) children.first().marginTop else 0
        var w = 0
        var h = 0
        var myW = paddingLeft
        var myH = paddingTop + if (childCount > 0) {
            val c = children.first()
            c.measuredHeight + c.marginTop + c.marginBottom
        } else 0
        children.forEach { view ->
            w = view.measuredWidth
            h = view.measuredHeight
            if (left + w + view.marginLeft + view.marginRight > maxW - paddingRight) {
                myW = Math.max(myW, left + view.marginLeft + view.marginRight)
                left = paddingLeft
                top += h + view.marginTop
                myH = Math.max(myH, top + view.measuredHeight + view.marginBottom)
            }
            left += view.marginLeft + w
        }

        setMeasuredDimension(maxW + paddingRight, myH + paddingBottom)
    }

    override fun updateTheme(colors: ChartColors) {
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingLeft
        var top = paddingTop + if (childCount > 0) children.first().marginTop else 0
        var w = 0
        var h = 0
        children.forEach { view ->
            w = view.measuredWidth
            h = view.measuredHeight
            if (left + w + view.marginLeft + view.marginRight > width - paddingRight) {
                left = paddingLeft
                top += h + view.marginTop
            }
            left += view.marginLeft
            view.layout(left, top, left + w, top + h)
            left += w
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}