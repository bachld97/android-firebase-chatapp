package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class AudioMessageMineItemViewModel(messageItem: MessageItem,
                                    private val clickCallback: MessageItemAdapter.ItemClickCallback) : MessageBaseItemViewModel(messageItem) {
    // fun getMessageContent(): String = messageItem.getContent()
    fun messageClicked() {
        clickCallback.onClick(messageItem)
    }
}
