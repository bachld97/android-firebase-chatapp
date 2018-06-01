package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class TextMessageOtherItemViewModel(messageItem: MessageItem,
                                    private val clickCallback: MessageItemAdapter.ItemClickCallback,
                                    private val longClickCallback: MessageItemAdapter.ItemLongClickCallback) : MessageOtherItemViewModel(messageItem) {
    fun getMessageContent(): String = messageItem.getContent()
    fun messageLongClicked(): Boolean {
        longClickCallback.onLongClick(messageItem)
        return true
    }

    fun messageClicked() {
        clickCallback.onClick(messageItem)
    }
}
