package com.solarexsoft.revealanimationdemo

import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by houruhou on 2020/6/15/3:50 PM
 * Desc:
 */
open class ItemPageViewHolder(val view: View): RecyclerView.ViewHolder(view), PageSelectListener, OnViewPagerVisibilityChangeListener {
    val ids = arrayOf(R.id.practice_question0, R.id.practice_question1, R.id.practice_question2, R.id.practice_question3, R.id.practice_question4, R.id.practice_question5)
    val questionViews = mutableListOf<TextView>()
    var pageSelected = false
    companion object {
        const val TAG = "ItemPageViewHolder"
    }
    init {
        Log.d(TAG, "view = $itemView")
    }
    @SuppressLint("ClickableViewAccessibility")
    fun bindData(position: Int) {
        val container = itemView as ConstraintLayout
        for (i in 0..4) {
            val textView = TextView(container.context)
            textView.id = ids[i]
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                56f.dp.toInt()
            )
            layoutParams.marginStart = 24f.dp.toInt()
            layoutParams.marginEnd = 24f.dp.toInt()
            layoutParams.startToStart = R.id.container
            layoutParams.endToEnd = R.id.container
            if (i == 0){
                layoutParams.topToBottom = R.id.guideLine
            } else {
                layoutParams.topToBottom = ids[i - 1]
                layoutParams.topMargin = 12f.dp.toInt()
            }
            textView.gravity = Gravity.CENTER
            textView.text = "$i"
            questionViews.add(textView)
            textView.setBackgroundResource(R.drawable.round_ffffff_12dp_radius)
            container.addView(textView, layoutParams)
            textView.setOnTouchListener { v, event ->
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    startSlideDownAnim()
                }
                true
            }
        }
    }

    private fun startSlideDownAnim() {
        val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.up_bottom)
        animation.fillAfter = true
        animation.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                Log.d(TAG, "onAnimationEnd")
            }

            override fun onAnimationStart(animation: Animation?) {

            }

        })
        questionViews.forEachIndexed { index, view ->
            view.startAnimation(animation)
        }
    }

    override fun onPageSelect(position: Int) {
        onSelected(position == adapterPosition)
    }
    open fun onSelected(isSelect: Boolean) {
        pageSelected = isSelect
        if (isSelect) {
//            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.bottom_up)
//            questionViews.forEach {
//                it.startAnimation(animation)
//            }
            questionViews.forEach {
                it.clearAnimation()
            }
        }
    }

    override fun onViewPagerVisibilityChange(isVisible: Boolean) {
        /*
        if (!isVisible) {
            itemView.visibility = View.INVISIBLE
        } else {
            itemView.visibility = View.VISIBLE
            if (pageSelected) {
                val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.bottom_up)
                questionViews.forEach {
                    it.startAnimation(animation)
                }
            }
        }*/
    }
}

class ItemPagerAdapter(
    private val onPageChange: OnPageChangeListener,
    private val viewPagerVisibilityChangeListeners: MutableList<OnViewPagerVisibilityChangeListener>
): RecyclerView.Adapter<ItemPageViewHolder>() {
    companion object {
        const val TAG = "ItemPagerAdapter"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPageViewHolder {
        return ItemPageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ItemPageViewHolder, position: Int) {
        holder.bindData(position)
        onPageChange.addPageSelectListener(holder)
        viewPagerVisibilityChangeListeners.add(holder)
    }

    override fun onViewAttachedToWindow(holder: ItemPageViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d(TAG, "onViewAttachedToWindow ${holder.adapterPosition}")
    }

    override fun onViewDetachedFromWindow(holder: ItemPageViewHolder) {
        super.onViewDetachedFromWindow(holder)
        Log.d(TAG, "onViewDetachedFromWindow ${holder.adapterPosition}")
    }

    override fun onViewRecycled(holder: ItemPageViewHolder) {
        super.onViewRecycled(holder)
        onPageChange.removePageSelectListener(holder)
        viewPagerVisibilityChangeListeners.remove(holder)
    }
}