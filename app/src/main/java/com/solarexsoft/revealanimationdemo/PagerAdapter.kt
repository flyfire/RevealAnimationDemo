package com.solarexsoft.revealanimationdemo

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
class ItemPageViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bindData(position: Int) {
        val tv = view.findViewById<TextView>(R.id.tv)
        tv.text = "$position"
        AnimationUtils.loadAnimation(view.context, R.anim.bottom_up).apply {
            duration = 500
            tv.startAnimation(this)
        }
    }
}

class ItemPagerAdapter: RecyclerView.Adapter<ItemPageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPageViewHolder {
        return ItemPageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ItemPageViewHolder, position: Int) {
        holder.bindData(position)
    }

}