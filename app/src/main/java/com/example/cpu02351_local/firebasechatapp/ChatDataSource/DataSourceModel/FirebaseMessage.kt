package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message

class FirebaseMessage : FirebaseObject() {

    companion object {
        @JvmStatic
        fun from(message: Message) : FirebaseMessage {
            return FirebaseMessage()
        }
    }

    override fun fromMap(id: String, value: Any?) {
        TODO()
    }

    fun toMessage() : Message {
        TODO()
    }

    override fun toMap(): Map<String, Any> {
        TODO()
    }
}