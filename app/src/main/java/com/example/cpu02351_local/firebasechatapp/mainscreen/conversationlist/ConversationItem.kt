package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ConversationItem(private val conversation: Conversation, private val curUserId: String,
                       val isRead: Boolean = true) : ListItem {
    override fun equalsItem(otherItem: ListItem): Boolean {
        return otherItem is ConversationItem && conversation.id == otherItem.conversation.id
    }

    override fun equalsContent(otherItem: ListItem): Boolean {
        return otherItem is ConversationItem && conversation.createdTime == otherItem.conversation.createdTime
    }

    fun getConversation(): Conversation = conversation

    fun computeDisplayInfo(info: HashMap<String, User>) {
        lastSenderName = if (conversation.lastMessage?.byUser == curUserId) {
            "You"
        } else {
            info[conversation.lastMessage?.byUser]?.name ?: ""
        }
        parseDisplayUrl(info)
        conversationDisplayName = parseConversationDisplayName(info)
    }

    var size = conversation.participantIds.size
    var lastMessagePreview = conversation.lastMessage?.getConversationPreviewDisplay() ?: ""
    var lastSenderName = conversation.lastMessage?.byUser ?: ""
    var elapseTimeDisplay = parseTime(conversation.lastMessage?.atTime)
    var shouldDisplaySender = conversation.participantIds.size > 2
    var displayAvaUrl = ""
    var displayAvaUrl1 = ""
    var displayAvaUrl2 = ""
    var displayAvaUrl3 = ""
    var displayAvaUrl4 = ""
    var conversationDisplayName = parseConversationDisplayName(null)

    private fun parseDisplayUrl(info: HashMap<String, User>?) {
        displayAvaUrl = conversation.participantIds
                .findLast { it != curUserId }!!
        displayAvaUrl = info?.get(displayAvaUrl)?.avaUrl ?: ""
        displayAvaUrl1 = info?.get(conversation.participantIds[1 % size])?.avaUrl ?: ""
        displayAvaUrl2 = info?.get(conversation.participantIds[2 % size])?.avaUrl ?: ""
        displayAvaUrl3 = info?.get(conversation.participantIds[3 % size])?.avaUrl ?: ""
        displayAvaUrl4 = info?.get(conversation.participantIds[4 % size])?.avaUrl ?: ""
    }

    private fun parseConversationDisplayName(info: HashMap<String, User>?): String {
        var res = conversation.participantIds.joinToString(", ") {
            info?.get(it)?.name ?: it
        }

        if (conversation.participantIds.size == 2) {
            res = conversation.participantIds
                    .findLast { it != curUserId }!!
            res = info?.get(res)?.name ?: res
        }
        return res
    }

    private fun parseTime(time: Long?): String {
        val createdTime = time ?: conversation.createdTime
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

    override fun equals(other: Any?): Boolean {
        return other is ConversationItem && other.conversation.id == conversation.id
    }
}