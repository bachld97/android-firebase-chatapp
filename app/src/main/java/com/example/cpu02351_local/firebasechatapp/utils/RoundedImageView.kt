package com.example.cpu02351_local.firebasechatapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.ImageView
import android.view.Display




class RoundedImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    // Max width = 2/3 screen width
    // Max height = 2/3 screen height

    var imageMaxWidth : Int = -1
    var imageMaxHeight: Int = -1

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        maxWidth = displayMetrics.widthPixels * 1 / 2
        maxHeight = displayMetrics.heightPixels * 1 / 2
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        if (params == null) {
            throw NullPointerException("Layout parameters cannot be null")
        }
        var finalWidth = params.width
        var finalHeight = params.height
        val ratio = 1.0f * finalWidth / finalHeight

        if (finalHeight > finalWidth) {
            finalHeight = maxHeight
            finalWidth = (finalHeight * ratio).toInt()
        } else {
            finalWidth = maxWidth
            finalHeight = (finalWidth / ratio).toInt()
        }

        params.width = finalWidth
        params.height = finalHeight
        super.setLayoutParams(params)
    }
}