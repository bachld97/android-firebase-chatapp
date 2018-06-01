package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

open class MessageOtherItemViewModel(messageItem: MessageItem) : MessageBaseItemViewModel(messageItem) {
    fun shouldDisplaySenderInfo(): Boolean =  messageItem.shouldDisplaySenderInfo
    fun getDisplaySenderName(): String = messageItem.senderName ?: messageItem.getSenderId()
    fun getDisplaySenderAvaUrl(): String = messageItem.senderAvaUrl ?: ""
}