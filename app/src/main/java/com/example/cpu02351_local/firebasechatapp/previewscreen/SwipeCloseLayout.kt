package com.example.cpu02351_local.firebasechatapp.previewscreen

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout

// TODO: Fling gesture
class SwipeCloseLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var screenHeight = -1
    private var flingThreshold = -1
    private var flingThresholdIgnoreInner = -1
    private var mTouchSlop = -1

    init {
        val vc = ViewConfiguration.get(context)!!
        mTouchSlop = vc.scaledTouchSlop
        flingThreshold = (vc.scaledMinimumFlingVelocity + vc.scaledMaximumFlingVelocity) / 6
        flingThresholdIgnoreInner = flingThreshold * 2
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
    }


    private fun smoothResetChildPosition() {
        this.animate()
                .translationY(0f)
                .start()
    }

    private fun smoothEndActivity(difY: Float) {
        val endPos = if (difY < 0) {
            -screenHeight.toFloat()
        } else {
            screenHeight.toFloat()
        }
        this.animate().translationY(endPos)
                .withEndAction {
                    (context as Activity).finish()
                }
                .start()
    }


    private var startY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startY = ev.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(ev.rawY - startY) - mTouchSlop > 0f) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val rawY = event!!.rawY
        val difY = rawY - startY
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startY = rawY
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                translationY = rawY - startY
            }
            MotionEvent.ACTION_UP -> {
                if (Math.abs(difY) > screenHeight / 4.5) {
                    smoothEndActivity(difY)
                } else {
                    smoothResetChildPosition()
                }
            }
        }

        return super.onTouchEvent(event)
    }
}

