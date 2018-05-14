package com.example.cpu02351_local.firebasechatapp.ChatViewModel.model

import java.util.*

class Conversation(val id: String) {
    var participantIds: List<String> = ArrayList()
    var createdTime = -1L

    init {
        participantIds = id.split(ID_DELIM)
    }

    companion object {
        const val ID_DELIM = "@"
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
        result = 31 * result + participantIds.hashCode()
        result = 31 * result + createdTime.hashCode()
        return result
    }
}