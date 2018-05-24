package com.example.cpu02351_local.firebasechatapp.model.firebasemodel

import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.model.Message

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
            this.atTime = valueMap[FirebaseHelper.TIME]!!.toLong()
            this.type = valueMap[FirebaseHelper.TYPE] as String
            this.byUser = valueMap[FirebaseHelper.BY_USER] as String
            this.content = valueMap[FirebaseHelper.CONTENT] as String
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
        res[FirebaseHelper.TIME] = atTime.toString()
        res[FirebaseHelper.TYPE] = type
        res[FirebaseHelper.BY_USER] = byUser
        res[FirebaseHelper.CONTENT] = content
        return res
    }

    fun toMessageFromMap(key: String, value: Any?): Message {
        this.fromMap(key, value)
        return this.toMessage()
    }
}