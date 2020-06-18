package com.solarexsoft.revealanimationdemo

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
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
    defStyleAttr: Int
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
    init {
        whiteColor = context?.resources?.getColor(android.R.color.white) ?: 0
        white30Percent = context?.resources?.getColor(R.color.white_30_percent) ?: 0
    }
    private fun calAnimStartStopPos() {
        widthPerProgress = (width - (total - 1) * gap)/total
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

    override fun onDraw(canvas: Canvas?) {

    }
}