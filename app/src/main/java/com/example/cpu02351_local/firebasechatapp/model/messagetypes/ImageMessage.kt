package com.example.cpu02351_local.firebasechatapp.model.messagetypes

import android.graphics.Bitmap
import android.net.Uri
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class ImageMessage(id: String = "", atTime: Long = -1L, byUser: String = "", content: String = "")
    : AbstractMessage(id, atTime, byUser, content) {

    constructor(w: Int, h: Int) : this() {
        width = w
        height = h
    }

    var cachedBitmap: Bitmap? = null
    var width = cachedBitmap?.width ?: 0
    var height = cachedBitmap?.height ?: 0
    var hasBitmap: Boolean = false
    lateinit var localUri: Uri

    override fun buildAdditionalContent(): HashMap<String, String>? {
        val content = HashMap<String, String>()
        content["width"] = width.toString()
        content["height"] = height.toString()
        return content
    }

    fun onBitmapLoaded(loadedBitmap: Bitmap) {
        cachedBitmap = loadedBitmap
        width = cachedBitmap!!.width
        height = cachedBitmap!!.height
        hasBitmap = true
    }

    override fun getType(): String {
        return "image"
    }

    override fun getConversationPreviewDisplay(): String {
        return "[Picture]"
    }
}