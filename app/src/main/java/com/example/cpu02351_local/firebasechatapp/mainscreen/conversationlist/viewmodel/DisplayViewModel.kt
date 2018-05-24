package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.viewmodel

import com.example.cpu02351_local.firebasechatapp.model.User

class DisplayViewModel(var users: HashMap<String, User>?,
                var participants: List<String>, var currentUser: String) {
    fun getAvaUrl(lastMessageSenderId : String) : String {
        var res = ""
        if (participants.size == 2) {
            res = participants.filter { it != currentUser }
                    .map { getSenderAvaUrl(it) }[0]
        } else {
            getSenderAvaUrl(lastMessageSenderId)
        }
        return res
    }

    fun getConversationName(): String {
        var res = participants.joinToString(", ") { getSenderName(it) }
        if (participants.size == 2) {
            res = participants
                    .filter {  it != currentUser }[0]
            res = getSenderName(res)
        }
        return res
    }

    fun getDisplaySender(): Boolean = participants.size > 2

    fun getSenderName(userId: String): String {
        return users?.get(userId)?.name ?: userId
    }

    private fun getSenderAvaUrl(userId: String) : String {
        return users?.get(userId)?.avaUrl ?: ""
    }
}