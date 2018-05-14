package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource

class FirebaseUser : FirebaseObject() {
    private lateinit var id: String
    lateinit var conversationIds: String
    private lateinit var name: String

    private lateinit var contacts: String

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        val valueMap = try {
            value as HashMap<String, String>
        } catch (e: TypeCastException) {
            null
        }
        if (valueMap != null) {
            this.conversationIds = valueMap[FirebaseChatDataSource.CONVERSATIONS] ?: ""
            this.name = valueMap[FirebaseChatDataSource.USERNAME] as String
            this.contacts = valueMap[FirebaseChatDataSource.CONTACTS] ?: ""
        }
    }

    fun toUser() : User {
        return User(id, name, conversationIds)
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, String>()
        res[FirebaseChatDataSource.CONTACTS] = contacts
        res[FirebaseChatDataSource.CONVERSATIONS] = conversationIds
        res[FirebaseChatDataSource.USERNAME] = name
        return res
    }
}