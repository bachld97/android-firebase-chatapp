package com.example.cpu02351_local.firebasechatapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import com.example.cpu02351_local.firebasechatapp.R

class VideoImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RoundedImageView(context, attrs, defStyleAttr) {

    val videoIcon = context.getDrawable(R.drawable.ic_send_video)!!
    private val iconSize = context.resources.getDimension(R.dimen.videoIcon).toInt()
    private val videoIconPadding = context.resources.getDimension(R.dimen.videoIconPadding).toInt()

     var videoIconPosition = Rect()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        videoIconPosition.left = right - iconSize - videoIconPadding
        videoIconPosition.top = top - videoIconPadding
        videoIconPosition.right = right - videoIconPadding
        videoIconPosition.bottom = top - iconSize - videoIconPadding
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        videoIcon.bounds = videoIconPosition
        videoIcon.draw(canvas)
    }
}