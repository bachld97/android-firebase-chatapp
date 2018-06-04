package com.example.cpu02351_local.firebasechatapp.model.messagetypes

import android.graphics.Bitmap
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class ImageMessage(id: String = "", atTime: Long = -1L, byUser: String = "", content: String = "")
    : AbstractMessage(id, atTime, byUser, content) {

    var cachedBitmap: Bitmap? = null
    var hasBitmap: Boolean = false

    fun onBitmapLoaded(loadedBitmap: Bitmap) {
        cachedBitmap = loadedBitmap
        hasBitmap = true
    }

    override fun getType(): String {
        return "image"
    }

    override fun getConversationPreviewDisplay(): String {
        // TODO: Extract into string resources?
        return "[Picture]"
    }
}