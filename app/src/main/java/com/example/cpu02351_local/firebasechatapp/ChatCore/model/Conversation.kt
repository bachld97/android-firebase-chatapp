package com.example.cpu02351_local.firebasechatapp.ChatCore.model

class Conversation(val id: String) {
    var participantIds: List<String>? = null
    var messages: List<Message>? = null
    var createdTime: String? = null

    override fun equals(other: Any?): Boolean {
        return other is Conversation && other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (participantIds?.hashCode() ?: 0)
        result = 31 * result + (messages?.hashCode() ?: 0)
        result = 31 * result + (createdTime?.hashCode() ?: 0)
        return result
    }
}