package com.solarexsoft.revealanimationdemo

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * Created by houruhou on 2020/6/18/4:23 PM
 * Desc:
 */

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
class PersonalizeLearningPagerIndicator @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var total: Int = 1
        set(value) {
            field = value
            calAnimStartStopPos()
        }
    var progress: Int = 1
        set(value) {
            field = value
            calAnimStartStopPos()
        }
    var animStartPos: Float = 0f
    var animStopPos: Float = 0f
    private val gap = 4f.dp
    private var widthPerProgress: Float = 0f
    var animProgress: Float = 0f
    var whiteColor: Int = 0
    var white30Percent: Int = 0
    var grayStart = 0f
    var grayEnd = 0f
    var whiteStart = 0f
    var whiteEnd = 0f
    lateinit var grayPaint: Paint
    lateinit var whitePaint: Paint

    init {
        whiteColor = context?.resources?.getColor(android.R.color.white) ?: 0
        white30Percent = context?.resources?.getColor(R.color.white_30_percent) ?: 0
        grayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        whitePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        grayPaint.color = context?.resources?.getColor(android.R.color.darker_gray) ?: 0
        whitePaint.color = context?.resources?.getColor(android.R.color.white) ?: 0
    }
    private fun calAnimStartStopPos() {
        widthPerProgress = (measuredWidth - (total - 1) * gap)/total
        animStartPos = progress * (widthPerProgress + gap)
        animStopPos = animStartPos + widthPerProgress
        ValueAnimator.ofFloat(animStartPos, animStopPos).apply {
            addUpdateListener {
                animProgress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calAnimStartStopPos()
    }

    override fun onDraw(canvas: Canvas) {
        drawGrayBg(canvas)
        drawWhiteProgress(canvas)
        drawAnimProgress(canvas)
    }

    private fun drawGrayBg(canvas: Canvas) {
        for (i in 0 until total) {
            grayStart = i * (widthPerProgress + gap)
            grayEnd = grayStart + widthPerProgress
            canvas.drawRoundRect(grayStart, 0f, grayEnd, height.toFloat(), 2f, 2f, grayPaint)
        }
    }

    private fun drawWhiteProgress(canvas: Canvas) {
        if (progress < 1) return
        for (i in 0 until progress) {
            whiteStart = i * (widthPerProgress + gap)
            whiteEnd = whiteStart + widthPerProgress
            canvas.drawRoundRect(whiteStart, 0f, whiteEnd, height.toFloat(),2f, 2f, whitePaint)
        }
    }

    private fun drawAnimProgress(canvas: Canvas) {
        canvas.drawRoundRect(animStartPos, 0f, animProgress, height.toFloat(), 2f, 2f, whitePaint)
    }
}