package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class TextMessageOtherItemViewModel(messageItem: MessageItem) : MessageOtherItemViewModel(messageItem) {
    fun getMessageContent(): String = messageItem.getContent()
}
