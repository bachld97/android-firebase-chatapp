package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation

class FirebaseConversation : FirebaseObject() {
    override fun toMap(): Map<String, Any> {
        TODO()
    }

    fun toConversation() : Conversation {
        return Conversation("ABC")
    }
}