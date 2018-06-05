package com.example.cpu02351_local.firebasechatapp.messagelist.model

import android.text.format.DateFormat
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

open class MessageItem(val message: AbstractMessage, var shouldDisplaySenderInfo: Boolean, var shouldDisplayTime: Boolean, val fromThisUser: Boolean) : ListItem {
    private val fromWhom = "_${if (fromThisUser) "mine" else "other"}"
    val displayTime: String = DateFormat.format("HH:mm", message.atTime).toString()
    var messTime = message.atTime

    var senderAvaUrl : String? = null
    var senderName: String? = null
    var isSending = message.isSending

    override fun equalsItem(otherItem: ListItem): Boolean {
        return otherItem is MessageItem && otherItem.message.id == message.id
    }

    override fun equalsContent(otherItem: ListItem): Boolean {
        return otherItem is MessageItem && otherItem.message.id == message.id &&
                otherItem.shouldDisplaySenderInfo == shouldDisplaySenderInfo &&
                otherItem.shouldDisplayTime == shouldDisplayTime &&
                otherItem.isSending == isSending
    }

    fun hasUserInfoChange(info: HashMap<String, User>): Boolean {
        if (!shouldDisplaySenderInfo)
            return false
        val senderId = getSenderId()
        senderAvaUrl = info[senderId]?.avaUrl
        senderName = info[senderId]?.name ?: senderId
        return true
    }

    fun getType(): String = "${message.getType()}$fromWhom"
    fun getContent() = message.content
    fun getSenderId() = message.byUser
}