package com.example.cpu02351_local.firebasechatapp.ChatCore.model

import java.util.*

class Conversation(val id: String) {
    var participantIds: List<String>? = null
    var createdTime = -1L

    companion object {
        private const val ID_DELIM = "$"
        @JvmStatic
        fun uniqueId(users: Array<User>) : String {
            if (users.size > 2) {
                return UUID.randomUUID().toString()
            }

            if (users[0].id < users[1].id) {
                return "${users[0].id}$ID_DELIM${users[1].id}"
            }
            return "${users[1].id}$ID_DELIM${users[0].id}"
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Conversation && other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (participantIds?.hashCode() ?: 0)
        result = 31 * result + createdTime.hashCode()
        return result
    }
}