package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User

class FirebaseUser : FirebaseObject() {
    var conversationIds = ""

    override fun fromMap(id: String, value: Any?) {
        TODO()
    }

    fun toUser() : User {
        TODO()
    }

    override fun toMap(): Map<String, Any> {
        TODO()
    }
}