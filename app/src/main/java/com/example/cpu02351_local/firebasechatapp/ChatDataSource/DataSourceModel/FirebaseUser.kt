package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatModel

class FirebaseUser : FirebaseObject() {
    private lateinit var id: String
    lateinit var conversationIds: String
    private lateinit var name: String

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        val valueMap = try {
            value as HashMap<String, String>
        } catch (e: TypeCastException) {
            null
        }
        if (valueMap != null) {
            this.conversationIds = valueMap[FirebaseChatModel.CONVERSATIONS] ?: ""
            this.name = valueMap[FirebaseChatModel.USERNAME] as String
        }
    }

    fun toUser() : User {
        return User(id, name, conversationIds)
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, String>()
        res[FirebaseChatModel.CONVERSATIONS] = conversationIds
        res[FirebaseChatModel.USERNAME] = name
        return res
    }
}