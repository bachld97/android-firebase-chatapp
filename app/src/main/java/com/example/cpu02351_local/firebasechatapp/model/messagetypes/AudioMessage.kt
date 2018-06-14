package com.example.cpu02351_local.firebasechatapp.model.messagetypes

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class AudioMessage(id: String ="", atTime: Long = -1L, byUser: String = "", content: String = "")
    : AbstractMessage(id, atTime, byUser, content) {

    override fun getType(): String {
        return "audio"
    }

    override fun getConversationPreviewDisplay(): String {
        return "[Voice message]"
    }
}
