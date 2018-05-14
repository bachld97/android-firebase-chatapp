package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource.Companion.DELIM

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
            res.userIds.addAll(conversation.participantIds)
            return res
        }
    }

    override fun fromMap(id: String, value: Any?) {
        this.id = id
        val valueMap = try {
             value as HashMap<String, String>
        } catch (e: TypeCastException) {
            Log.d("BUG_FOUND", "FirebaseConversation: Cannot load map")
            null
        }
        if (valueMap != null) {
            lastModified = valueMap[FirebaseChatDataSource.LAST_MOD]!!.toLong()
            userIds.clear()
            userIds.addAll((valueMap[FirebaseChatDataSource.BY_USERS] as String).split(DELIM))
        }
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, Any>()
        // res[FirebaseChatDataSource.LAST_MOD] = lastModified
        res[FirebaseChatDataSource.BY_USERS] = userIds.joinToString(DELIM)
        return res
    }

    fun toConversation() : Conversation {
        val res = Conversation(id)
        res.participantIds = userIds
        res.createdTime = lastModified
        return res
    }
}