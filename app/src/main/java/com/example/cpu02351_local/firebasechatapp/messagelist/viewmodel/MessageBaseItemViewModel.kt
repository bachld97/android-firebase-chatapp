package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem


open class MessageBaseItemViewModel(var messageItem: MessageItem) {
    fun shouldDisplayTime(): Boolean = messageItem.shouldDisplayTime
    fun getDisplayTime(): String = messageItem.displayTime
    fun isMessageSending(): Boolean = messageItem.isSending
}