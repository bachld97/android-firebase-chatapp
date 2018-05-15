package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message

class FirebaseMessage : FirebaseObject() {
    private lateinit var id: String
    private lateinit var type: String
    private lateinit var content: String
    private lateinit var byUser: String
    private var atTime = 0L

    companion object {
        @JvmStatic
        fun from(message: Message) : FirebaseMessage {
            val res = FirebaseMessage()
            res.id = message.id
            res.atTime = message.atTime
            res.type = message.getType()
            res.byUser = message.byUser!!
            res.content = message.content
            return res
        }
    }

    override fun fromMap(id: String, value: Any?) {
        val valueMap = try {
            value as HashMap<String, String>
        } catch (e: TypeCastException) {
            null
        }
        if (valueMap != null) {
            this.id = id
            this.atTime = valueMap[FirebaseChatDataSource.TIME]!!.toLong()
            this.type = valueMap[FirebaseChatDataSource.TYPE] as String
            this.byUser = valueMap[FirebaseChatDataSource.BY_USER] as String
            this.content = valueMap[FirebaseChatDataSource.CONTENT] as String
        }
    }

    fun toMessage() : Message {
        // Later add switch type to return correct type of user
        val res = Message(id)
        res.atTime = atTime
        res.content = content
        res.byUser = byUser
        return res
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, String>()
        res[FirebaseChatDataSource.TIME] = atTime.toString()
        res[FirebaseChatDataSource.TYPE] = type
        res[FirebaseChatDataSource.BY_USER] = byUser
        res[FirebaseChatDataSource.CONTENT] = content
        return res
    }
}