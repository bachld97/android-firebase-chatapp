package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class TextMessageMineItemViewModel(messageItem: MessageItem) : MessageBaseItemViewModel(messageItem) {
    fun getMessageContent(): String = messageItem.getContent()
}
