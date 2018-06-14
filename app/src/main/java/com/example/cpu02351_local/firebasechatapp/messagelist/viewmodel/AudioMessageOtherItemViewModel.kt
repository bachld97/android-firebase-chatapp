package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class AudioMessageOtherItemViewModel(messageItem: MessageItem,
                                     private val clickCallback: MessageItemAdapter.ItemClickCallback) : MessageOtherItemViewModel(messageItem) {
    fun messageClicked() {
        clickCallback.onClick(messageItem)
    }
}
