package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ConversationItem(private val conversation: Conversation, private val curUserId: String) : ListItem {
    fun updateElapse() {
        val curTime = System.currentTimeMillis()
        if (curTime - lastUpdateTime >= timeThreshold) {
            elapseTimeDisplay = parseTime(conversation.lastMessage?.atTime, curTime)
        }
    }

    private var lastUpdateTime = 0L
    private var timeThreshold = 60000


    override fun equalsItem(otherItem: ListItem): Boolean {
        return otherItem is ConversationItem && conversation.id == otherItem.conversation.id
    }

    override fun equalsContent(otherItem: ListItem): Boolean {
        return otherItem is ConversationItem && conversation.createdTime == otherItem.conversation.createdTime
    }

    fun getConversation(): Conversation = conversation

    fun hasUserInfoChange(info: HashMap<String, User>): Boolean {
        val oldSender = lastSenderName
        lastSenderName = if (conversation.lastMessage?.byUser == curUserId) {
            "You"
        } else {
            info[conversation.lastMessage?.byUser]?.name ?: ""
        }
        val avaUrlChanged = hasUrlChange(info)
        val oldConversationName = conversationDisplayName
        conversationDisplayName = parseConversationDisplayName(info)
        val oldTimeDisplay = elapseTimeDisplay
        updateElapse()
        return avaUrlChanged || oldSender != lastSenderName || oldConversationName != conversationDisplayName || oldTimeDisplay != elapseTimeDisplay
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

    private fun hasUrlChange(info: HashMap<String, User>?) : Boolean {

        val oldAvaUrl = displayAvaUrl
        val oldAvaUrl1 = displayAvaUrl1
        val oldAvaUrl2 = displayAvaUrl2
        val oldAvaUrl3 = displayAvaUrl3
        val oldAvaUrl4 = displayAvaUrl4

        displayAvaUrl = conversation.participantIds
                .findLast { it != curUserId }!!
        displayAvaUrl = info?.get(displayAvaUrl)?.avaUrl ?: ""
        displayAvaUrl1 = info?.get(conversation.participantIds[1 % size])?.avaUrl ?: ""
        displayAvaUrl2 = info?.get(conversation.participantIds[2 % size])?.avaUrl ?: ""
        displayAvaUrl3 = info?.get(conversation.participantIds[3 % size])?.avaUrl ?: ""
        displayAvaUrl4 = info?.get(conversation.participantIds[4 % size])?.avaUrl ?: ""

        return oldAvaUrl != displayAvaUrl ||
                oldAvaUrl1 != displayAvaUrl1 ||
                oldAvaUrl2 != displayAvaUrl2 ||
                oldAvaUrl3 != displayAvaUrl3 ||
                oldAvaUrl4 != displayAvaUrl4
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

    private fun parseTime(time: Long?, curTime: Long = System.currentTimeMillis()): String {
        val createdTime = time ?: conversation.createdTime
        val interval = curTime - createdTime
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

    var isRead = conversation.isRead

    override fun equals(other: Any?): Boolean {
        return other is ConversationItem && other.conversation.id == conversation.id
    }
}