package com.example.cpu02351_local.firebasechatapp.model.firebasemodel.messagetypes

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class TextMessage(id: String = "", atTime: Long = -1L, byUser: String = "", content: String = "")
            : AbstractMessage(id, atTime, byUser, content) {
    override fun getType(): String {
        return "text"
    }

    override fun getConversationPreviewDisplay(): String {
        return content
    }
}