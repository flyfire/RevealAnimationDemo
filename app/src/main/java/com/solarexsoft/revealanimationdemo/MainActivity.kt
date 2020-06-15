package com.solarexsoft.revealanimationdemo

import android.animation.Animator
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.hypot

class MainActivity : AppCompatActivity() {
    var toOpen = true
    lateinit var fab: FloatingActionButton
    lateinit var vp: ViewPager2
    lateinit var adapter: ItemPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp = findViewById<ViewPager2>(R.id.vp_main)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            openOrCloseViewPager(toOpen)
            toOpen = !toOpen
        }
        adapter = ItemPagerAdapter()
        vp.adapter = adapter
        vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        var isFirstPage = false
        var isDragging = false
        vp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                adapter.notifyItemChanged(position)
                isFirstPage = position == 0
            }

            override fun onPageScrollStateChanged(state: Int) {
                isDragging = ViewPager2.SCROLL_STATE_DRAGGING == state
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (isFirstPage && isDragging && positionOffsetPixels == 0) {
                    openOrCloseViewPager(false)
                    toOpen = true
                }
            }
        })
    }

    fun openOrCloseViewPager(toOpen: Boolean) {
        val centerX = fab.x + fab.width/2;
        val centerY = fab.y + fab.height/2;
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        val width = point.x
        val height = point.y/2
        val hypot = hypot(width.toDouble(), height.toDouble())
        if (toOpen) {
            val animator = ViewAnimationUtils.createCircularReveal(vp, centerX.toInt(), centerY.toInt(), 0.0f, hypot.toFloat())
            animator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    adapter.notifyItemChanged(vp.currentItem)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            animator.duration = 1000
            vp.visibility = View.VISIBLE
            animator.start()
        } else {
            val animator = ViewAnimationUtils.createCircularReveal(vp, centerX.toInt(), centerY.toInt(), hypot.toFloat(), 0.0f)
            animator.duration = 1000
            animator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    vp.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            animator.start()
        }
    }
}
