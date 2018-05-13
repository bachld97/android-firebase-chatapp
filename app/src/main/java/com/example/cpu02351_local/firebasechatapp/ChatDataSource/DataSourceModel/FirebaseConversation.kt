package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation

class FirebaseConversation : FirebaseObject() {

    companion object {
        @JvmStatic
        fun from(conversation: Conversation) : FirebaseConversation {
            TODO()
        }
    }

    override fun load(id: String, value: Any?) {
        TODO()
    }


    override fun toMap(): Map<String, Any> {
        TODO()
    }

    fun toConversation() : Conversation {
        return Conversation("ABC")
    }
}