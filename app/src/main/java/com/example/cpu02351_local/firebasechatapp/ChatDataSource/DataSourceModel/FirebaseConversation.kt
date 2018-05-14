package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatModel
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatModel.Companion.DELIM

class FirebaseConversation : FirebaseObject() {

    private lateinit var id: String
    private var lastModified = -1L
    private var userIds = ArrayList<String>()

    companion object {
        @JvmStatic
        fun from(conversation: Conversation) : FirebaseConversation {
            val res = FirebaseConversation()
            res.id = conversation.id
            res.lastModified = conversation.createdTime
            res.userIds.clear()
            res.userIds.addAll(conversation.participantIds!!)
            return res
        }
    }

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        val valueMap = try {
             value as HashMap<String, Any>
        } catch (e: TypeCastException) {
            Log.d("BUG_FOUND", "FirebaseConversation: Cannot load map")
            null
        }
        if (valueMap != null) {
            lastModified = valueMap[FirebaseChatModel.LAST_MOD] as Long
            userIds.clear()
            userIds.addAll((valueMap[FirebaseChatModel.PARTICIPANTS] as String).split(DELIM))
        }
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, Any>()
        res[FirebaseChatModel.LAST_MOD] = lastModified
        res[FirebaseChatModel.PARTICIPANTS] = userIds.joinToString(DELIM)
        return res
    }

    fun toConversation() : Conversation {
        val res = Conversation(id)
        res.participantIds = userIds
        res.createdTime = lastModified
        return res
    }
}