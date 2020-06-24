package com.solarexsoft.revealanimationdemo

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.hypot

class MainActivity : AppCompatActivity(), PageChangeListener {
    companion object {
        const val TAG = "MainActivity"
    }
    var toOpen = true
    lateinit var fab: FloatingActionButton
    lateinit var vp: ViewPager2
    lateinit var adapter: ItemPagerAdapter
    lateinit var indicator: PersonalizeLearningPagerIndicator
    var lastPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp = findViewById<ViewPager2>(R.id.vp_main)
        indicator = findViewById(R.id.indicator)
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            openOrCloseViewPager(toOpen)
            toOpen = !toOpen
        }

        val rvField = vp::class.java.getDeclaredField("mRecyclerView")
        rvField.isAccessible = true
        val recyclerView = rvField.get(vp) as RecyclerView
        recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                val position = recyclerView.getChildAdapterPosition(view)
                Log.d(TAG, "child view detached $position $view")
            }

            override fun onChildViewAttachedToWindow(view: View) {
                val position = recyclerView.getChildAdapterPosition(view)
                Log.d(TAG, "child view attached $position $view")
            }

        })
        val pageChangeCallback = OnPageChangeListener(this)
        vp.registerOnPageChangeCallback(pageChangeCallback)
        adapter = ItemPagerAdapter(pageChangeCallback)
        vp.adapter = adapter
        vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vp.offscreenPageLimit = 3
        indicator.total = 3
    }

    private fun notifyPageChanged(position: Int) {
        indicator.progress = position
        if (lastPosition != position) {
            Log.d(TAG, "last position = $lastPosition")
            lastPosition = position
        }
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
                    Log.d(TAG, "onAnimationEnd called")
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

    override fun onFirstDragging() {
        openOrCloseViewPager(false)
    }

    override fun onPageChange(position: Int) {
        notifyPageChanged(position)
    }
}

interface PageSelectListener {
    fun onPageSelect(position: Int)
}

interface PageChangeListener {
    fun onFirstDragging()
    fun onPageChange(position: Int)
}

class OnPageChangeListener(private val pageChangeListener: PageChangeListener):
    ViewPager2.OnPageChangeCallback() {
    private var isDragging = false
    private var isFirstPage = false
    private val pageSelectListeners = mutableListOf<PageSelectListener>()
    override fun onPageScrollStateChanged(state: Int) {
        isDragging = ViewPager2.SCROLL_STATE_DRAGGING == state
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (isFirstPage && isDragging && positionOffsetPixels == 0) {
            pageChangeListener.onFirstDragging()
        }
    }

    override fun onPageSelected(position: Int) {
        isFirstPage = position == 0
        pageChangeListener.onPageChange(position)
        pageSelectListeners.forEach{
            it.onPageSelect(position)
        }
    }

    fun addPageSelectListener(pageSelectListener: PageSelectListener) {
        pageSelectListeners.add(pageSelectListener)
    }
    fun removePageSelectListener(pageSelectListener: PageSelectListener) {
        pageSelectListeners.remove(pageSelectListener)
    }
}
