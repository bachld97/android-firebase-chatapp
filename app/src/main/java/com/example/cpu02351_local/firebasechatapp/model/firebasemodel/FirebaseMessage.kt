package com.example.cpu02351_local.firebasechatapp.model.firebasemodel

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.TextMessage
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper

class FirebaseMessage : FirebaseObject() {
    private lateinit var id: String
    private lateinit var type: String
    private lateinit var content: String
    private lateinit var byUser: String
    private var additionalContent: HashMap<String, String>? = null
    private var atTime = 0L

    companion object {
        @JvmStatic
        fun from(message: AbstractMessage) : FirebaseMessage {
            val res = FirebaseMessage()
            res.id = message.id
            res.atTime = message.atTime
            res.type = message.getType()
            res.byUser = message.byUser
            res.content = message.content
            res.additionalContent = message.buildAdditionalContent()
            return res
        }
    }

    override fun fromMap(id: String, value: Any?) {
        val valueMap = try {
            value as HashMap<*, *>
        } catch (e: TypeCastException) {
            null
        }
        if (valueMap != null) {
            this.id = id
            this.atTime = (valueMap[FirebaseHelper.TIME] as String).toLong()
            this.type = valueMap[FirebaseHelper.TYPE] as String
            this.byUser = valueMap[FirebaseHelper.BY_USER] as String
            this.content = valueMap[FirebaseHelper.CONTENT] as String
            this.additionalContent = valueMap[FirebaseHelper.ADDITIONAL] as HashMap<String, String>?
        }
    }

    fun toMessage() : AbstractMessage {
        // Later add switch type to return correct type of user
        return when (type) {
            "text" -> TextMessage(id, atTime, byUser, content)
            "image" -> {
                val tem = ImageMessage(id, atTime, byUser, content)
                tem.width = additionalContent!!["width"]!!.toInt()
                tem.height = additionalContent!!["height"]!!.toInt()
                return tem
            }
            else -> throw IllegalStateException()
        }
    }

    override fun toMap(): Map<String, Any> {
        val res = HashMap<String, Any>()
        res[FirebaseHelper.TIME] = atTime.toString()
        res[FirebaseHelper.TYPE] = type
        res[FirebaseHelper.BY_USER] = byUser
        res[FirebaseHelper.CONTENT] = content
        if (additionalContent != null)
           res[FirebaseHelper.ADDITIONAL] = additionalContent!!
        return res
    }

    fun toMessageFromMap(key: String, value: Any?): AbstractMessage {
        this.fromMap(key, value)
        return this.toMessage()
    }
}