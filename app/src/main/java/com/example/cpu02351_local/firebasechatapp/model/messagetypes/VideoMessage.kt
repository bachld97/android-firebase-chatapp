package com.example.cpu02351_local.firebasechatapp.model.messagetypes

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class VideoMessage(id: String ="", atTime: Long = -1L, byUser: String = "", content: String = "")
    : AbstractMessage(id, atTime, byUser, content) {

    constructor(w: Int, h: Int, thumbnailLink: String) : this() {
        width = w
        height = h
        this.thumbnailLink = thumbnailLink
    }

    override fun getType(): String {
        return "video"
    }

    override fun getConversationPreviewDisplay(): String {
        return "[Video]"
    }

    override fun buildAdditionalContent(): HashMap<String, String>? {
        val content = HashMap<String, String>()
        content["width"] = width.toString()
        content["height"] = height.toString()
        content["thumbnail"] = thumbnailLink
        return content
    }

    var width = 0
    var height = 0

    var thumbnailLink: String = ""
    fun setMetadata(w: Int, h: Int) {
        width = w
        height = h
    }
}