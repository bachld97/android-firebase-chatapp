package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.viewmodel

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User

class DisplayViewModel(var users: HashMap<String, User>?,
                var participants: List<String>, var currentUser: String) {
    fun getAvaUrl(lastMessageSenderId : String?) : String {
        var res = ""
        if (participants.size == 2) {
            res = participants.filter { it != currentUser }
                    .map { getSenderAvaUrl(it) }[0]
        } else {
            getSenderAvaUrl(lastMessageSenderId)
        }
        return res
    }

    fun parseTime(conversation: Conversation): String {
        conversation.createdTime = conversation.lastMessage?.atTime ?: conversation.createdTime
        val createdTime = conversation.createdTime
        val interval = System.currentTimeMillis() - createdTime
        val minutes = interval / (1000 * 60)
        val hours = minutes / 60
        val days= hours / 24

        return when {
            minutes == 0L -> "Just now"
            hours == 0L -> {
                if (minutes == 1L) "$minutes minute ago"
                else "$minutes minutes ago"
            }
            days == 0L -> {
                if (hours == 1L) "$hours hour ago"
                else "$hours hours ago"
            }
            else -> {
                if (days == 1L) "$days day ago"
                else "$days days ago"
            }
        }
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

    fun getSenderName(userId: String?): String {
        if (userId == null) {
            return ""
        }
        return users?.get(userId)?.name ?: userId
    }

    private fun getSenderAvaUrl(userId: String?) : String {
        return users?.get(userId)?.avaUrl ?: ""
    }
}