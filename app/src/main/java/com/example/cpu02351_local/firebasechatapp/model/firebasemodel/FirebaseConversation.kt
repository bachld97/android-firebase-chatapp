@file:Suppress("UNCHECKED_CAST")

package com.example.cpu02351_local.firebasechatapp.model.firebasemodel

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.DELIM
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.LAST_READ
import com.google.firebase.database.DataSnapshot

class FirebaseConversation : FirebaseObject() {

    private lateinit var id: String
    private var lastModified = -1L
    private var userIds = ArrayList<String>()
    private var lastRead = HashMap<String, String>()

    var lastMessage: FirebaseMessage? = null

    companion object {
        @JvmStatic
        fun from(conversation: Conversation) : FirebaseConversation {
            val res = FirebaseConversation()
            res.id = conversation.id
            res.lastModified = conversation.createdTime
            res.userIds.clear()
            res.userIds.addAll(conversation.participantIds)
            return res
        }
    }

    fun toConversationFromMap(id: String, value: Any?, curUser: String) : Conversation {
        this.fromMap(id, value)
        return this.toConversation(curUser)
    }

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        val valueMap = try {
             value as HashMap<String, Any>
        } catch (e: TypeCastException) {
            null
        }
        if (valueMap != null) {
            lastModified = (valueMap[FirebaseHelper.LAST_MOD] as String? ?: "-1").toLong()
            userIds.clear()
            userIds.addAll((valueMap[FirebaseHelper.BY_USERS] as String).split(DELIM))
            lastRead.putAll(valueMap[LAST_READ] as HashMap<String, String>? ?: HashMap())
        }
    }

    fun parseLastMess(last: DataSnapshot?): FirebaseConversation? {
        if (last == null) return null
        val mess = FirebaseMessage()
        mess.fromMap(last.key!!, last.value)
        this.lastMessage = mess
        return this
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, Any>()
        res[FirebaseHelper.BY_USERS] = userIds.joinToString(DELIM)
        return res
    }

    private fun toConversation(userId: String) : Conversation {
        val res = Conversation(id)
        res.participantIds = userIds
        res.createdTime = lastModified
        res.lastMessage = lastMessage?.toMessage()
        res.lastRead = lastRead
        res.isRead = lastRead[userId] != null && (res.lastMessage?.id == lastRead[userId])
        return res
    }
}