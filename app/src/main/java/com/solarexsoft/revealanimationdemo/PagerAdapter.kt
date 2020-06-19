package com.solarexsoft.revealanimationdemo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by houruhou on 2020/6/15/3:50 PM
 * Desc:
 */
open class ItemPageViewHolder(val view: View): RecyclerView.ViewHolder(view), PageSelectListener {

    companion object {
        const val TAG = "ItemPageViewHolder"
    }
    init {
        Log.d(TAG, "view = $itemView")
    }
    fun bindData(position: Int) {
        Log.d(TAG, "bindData position = $position")
        val tv = view.findViewById<TextView>(R.id.tv)
        tv.text = "$position"
        AnimationUtils.loadAnimation(view.context, R.anim.bottom_up).apply {
            duration = 500
            tv.startAnimation(this)
        }
    }

    override fun onPageSelect(position: Int) {
        onSelected(position == adapterPosition)
    }
    open fun onSelected(isSelect: Boolean) = Unit
}

class ItemPagerAdapter(
    private val onPageChange: OnPageChangeListener
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
    }
}