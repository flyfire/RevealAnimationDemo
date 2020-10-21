package com.solarexsoft.revealanimationdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_vp_in_vp.*
import kotlinx.android.synthetic.main.item_nested_vp.view.*

/**
 * Created by houruhou on 2020/10/21/3:54 PM
 * Desc:
 */
class VpNestedInVpActivity : AppCompatActivity(), PageChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vp_in_vp)
        vp_main.registerOnPageChangeCallback(OnPageChangeListener(this))
        vp_main.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp_main.adapter = NestedVpAdapter(10)
    }

    override fun onFirstDragging() {

    }

    override fun onPageChange(position: Int) {
        indicator.progress = position
    }
}

class NestedVpAdapter(private val count: Int): RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return BaseViewHolder.type(position)
    }

}

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        private const val ITEM_NORMAL = 0
        private const val ITEM_NESTED = 1

        fun type(position: Int): Int {
            return if (position % 2 == 0) {
                ITEM_NORMAL
            } else {
                ITEM_NESTED
            }
        }

        fun create(viewGroup: ViewGroup, type: Int): BaseViewHolder {
            return when (type) {
                ITEM_NORMAL-> {
                    NormalViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_normal, viewGroup, false))
                }
                else -> {
                    NestedParentViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_nested_vp, viewGroup, false))
                }
            }
        }
    }
    abstract fun bind()
}

class NormalViewHolder(view: View): BaseViewHolder(view) {
    override fun bind() {
    }
}

class NestedParentViewHolder(view: View): BaseViewHolder(view) {
    override fun bind() {
        itemView.vp_content.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        itemView.vp_content.adapter = NestedVpAdapter(5)
    }
}