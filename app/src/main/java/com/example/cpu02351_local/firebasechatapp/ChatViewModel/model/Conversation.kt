package com.example.cpu02351_local.firebasechatapp.ChatViewModel.model

import java.util.*

class Conversation(val id: String, users: Array<User> = arrayOf()) {
    var participantIds: List<String> = ArrayList()
    var createdTime = -1L

    init {
        participantIds = users.joinToString("$").split("$")
    }

    companion object {
        const val ID_DELIM = "@"
        @JvmStatic
        fun uniqueId(users: Array<User>) : String {
            if (users.size > 2) {
                return UUID.randomUUID().toString()
            }
             users.sort()
            return users.joinToString(ID_DELIM).trim(ID_DELIM[0])
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Conversation && other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + participantIds.hashCode()
        result = 31 * result + createdTime.hashCode()
        return result
    }
}