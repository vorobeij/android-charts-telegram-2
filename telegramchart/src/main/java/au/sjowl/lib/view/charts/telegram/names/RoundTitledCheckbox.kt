package au.sjowl.lib.view.charts.telegram.names

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import au.sjowl.lib.view.charts.telegram.ThemedView
import au.sjowl.lib.view.charts.telegram.params.ChartColors
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sp

class RoundTitledCheckbox : View, ThemedView {

    var checked: Boolean = false

    var chart: ChartItem? = null

    var animationDuration = 120L

    var title = "Android"

    @ColorInt
    var color: Int = Color.parseColor("#88ba52")
        set(value) {
            field = value
            lp.paintBackground.color = value
            animColor.set(color, Color.WHITE, color)
            animC = colorAnim(animColor)
        }

    private val animFloat = AnimatedPropertyF(0f, 0f, 1f)

    private val animColor = AnimatedPropertyInt(color, Color.WHITE, color)

    private val anim = valueAnim(animFloat)

    private var animC = colorAnim(animColor)

    private var onCheckedChangedListener: ((checked: Boolean) -> Unit)? = null

    private val lp = LayoutParams(this)

    private val tick = Tick()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lp.measure()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        lp.measureTitle(title)
        setMeasuredDimension(lp.width(), lp.height())
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)

        lp.setupTickRect(animFloat.value)
        tick.draw(lp.tickRect, canvas, lp.paintTick)

        drawTitle(canvas)
    }

    override fun updateTheme(colors: ChartColors) {
    }

    override fun onDetachedFromWindow() {
        onCheckedChangedListener = null
        super.onDetachedFromWindow()
    }

    fun check(value: Boolean, toAnimate: Boolean) {
        checked = value

        anim.cancel()
        animC.cancel()

        if (value) {
            anim.start()
            animC.start()
        } else {
            anim.reverse()
            animC.reverse()
        }

        if (!toAnimate) {
            anim.end()
            animC.end()
        }
    }

    fun onCheckedChangedListener(listener: ((checked: Boolean) -> Unit)?) {
        onCheckedChangedListener = listener
    }

    fun bind(
        chartItem: ChartItem,
        onChecked: ((chartItem: ChartItem, checked: Boolean) -> Unit),
        onLongClick: ((chartItem: ChartItem) -> Unit)
    ) {
        chart = chartItem
        onCheckedChangedListener { checked ->
            onChecked(chartItem, checked)
        }
        setOnLongClickListener {
            onLongClick(chartItem)
            true
        }
        color = chartItem.color
        check(chartItem.enabled, false)
        title = chartItem.name
    }

    private fun drawBackground(canvas: Canvas) {
        lp.paintBackground.alpha = (animFloat.value * 255).toInt()
        lp.paintBackground.style = Paint.Style.FILL
        canvas.drawRoundRect(lp.rectBackground, lp.radius, lp.radius, lp.paintBackground)
        lp.paintBackground.alpha = 255
        lp.paintBackground.style = Paint.Style.STROKE
        canvas.drawRoundRect(lp.rectBackground, lp.radius, lp.radius, lp.paintBackground)
    }

    private fun drawTitle(canvas: Canvas) {
        lp.paintTitle.color = animColor.value
        canvas.drawText(title, lp.titleOffsetLeft(animFloat.value), lp.titleOffsetTop, lp.paintTitle)
    }

    private fun valueAnim(animatedProperty: AnimatedPropertyF): ValueAnimator {
        return ValueAnimator.ofFloat(animatedProperty.from, animatedProperty.to).apply {
            duration = animationDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                animatedProperty.value = it.animatedValue as Float
                this@RoundTitledCheckbox.invalidate()
            }
        }
    }

    private fun colorAnim(animatedProperty: AnimatedPropertyInt) = ValueAnimator().apply {
        addUpdateListener {
            animatedProperty.value = it.animatedValue as Int
        }
        setIntValues(animatedProperty.from, animatedProperty.to)
        setEvaluator(ArgbEvaluator())
        duration = animationDuration
    }

    private fun init() {
        onClick {
            check(!checked, true)
            onCheckedChangedListener?.invoke(checked)
        }
        setPadding(context.dip(7), context.dip(13), context.dip(7), context.dip(13))
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    class Tick {
        private val points = floatArrayOf(
            0.77f, 0.29f,
            0.42f, 0.75f,
            0.25f, 0.54f
        )

        private val pathTick = Path()

        fun draw(rect: Rect, canvas: Canvas, paint: Paint) {
            pathTick.reset()
            val dx = rect.left
            val dy = rect.top
            pathTick.moveTo(points[0] * rect.width() + dx, points[1] * rect.height() + dy)
            for (i in 2 until points.size step 2) {
                pathTick.lineTo(points[i] * rect.width() + dx, points[i + 1] * rect.height() + dy)
            }
            canvas.drawPath(pathTick, paint)
        }
    }

    class LayoutParams(val v: View) {

        var textSize: Float = context.sp(14).toFloat()
            set(value) {
                field = value
                paintTitle.textSize = value
            }

        var iconSize: Int = context.dip(24)

        val paintBackground = Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
            strokeWidth = context.dip(1.7f).toFloat()
        }

        val paintTick = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = context.dip(2).toFloat()
            pathEffect = CornerPathEffect(2f)
            strokeCap = Paint.Cap.ROUND
        }

        val paintTitle = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            textSize = this@LayoutParams.textSize
        }

        val rectBackground = RectF()

        val radius get() = rectBackground.height() * 0.5f

        val iconDy get() = (height() - iconSize) / 2f

        val titleOffsetTop get() = (v.height - v.paddingBottom).toFloat()

        val tickRect = Rect()

        val rectTitle = Rect()

        val titleOffsetLeft get() = (v.paddingLeft + iconSize + v.paddingLeft).toFloat()

        private val context get() = v.context

        private val heightTitleRect = Rect()

        fun setupTickRect(anim: Float) {
            with(tickRect) {
                left = (anim * (iconSize + v.paddingLeft) - iconSize).toInt()
                right = left + iconSize
                top = iconDy.toInt()
                bottom = top + iconSize
            }
        }

        fun measureTitle(title: String) {
            paintTitle.getTextBounds(title, 0, title.length, rectTitle)
            paintTitle.getTextBounds("O", 0, 1, heightTitleRect)
        }

        fun titleOffsetLeft(anim: Float): Float {
            val desiredOffset = anim * (iconSize + v.paddingLeft)
            val minOffset = (width() - rectTitle.width()) / 2f
            return if (desiredOffset < minOffset) minOffset else desiredOffset
        }

        fun measure() {
            val d = paintBackground.strokeWidth
            rectBackground.left = d
            rectBackground.top = d
            rectBackground.bottom = height().toFloat() - d
            rectBackground.right = width().toFloat() - d
        }

        fun height(): Int = (heightTitleRect.height() + v.paddingTop + v.paddingBottom + paintBackground.strokeWidth).toInt()

        fun width(): Int = (titleOffsetLeft.toInt() + rectTitle.width() + v.paddingRight + paintBackground.strokeWidth).toInt()
    }
}