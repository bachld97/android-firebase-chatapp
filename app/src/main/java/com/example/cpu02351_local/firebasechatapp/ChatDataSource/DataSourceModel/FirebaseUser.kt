package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User

@Suppress("UNCHECKED_CAST")
class FirebaseUser(
        var id: String = "",
        private var password: String ="",
        var conversationIds: String = "",
        var name: String = id,
        private var contacts: String = "",
        var avaUrl: String ="") : FirebaseObject() {

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        this.name = id

        val valueMap = try {
            value as HashMap<String, String>
        } catch (e: TypeCastException) {
            null
        }

        if (valueMap != null) {
            this.password = valueMap[FirebaseHelper.PASSWORD] ?: ""
            this.conversationIds = valueMap[FirebaseHelper.CONVERSATIONS] ?: ""
            this.name = valueMap[FirebaseHelper.USERNAME] ?: id
            this.contacts = valueMap[FirebaseHelper.CONTACTS] ?: ""
            this.avaUrl = valueMap[FirebaseHelper.AVA_URL] ?: ""
        }
    }

    fun toUser() : User {
        return User(id, name, conversationIds, avaUrl)
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, String>()
        res[FirebaseHelper.AVA_URL] = avaUrl
        res[FirebaseHelper.CONTACTS] = contacts
        res[FirebaseHelper.CONVERSATIONS] = conversationIds
        res[FirebaseHelper.USERNAME] = name
        res[FirebaseHelper.PASSWORD] = password
        return res
    }
}