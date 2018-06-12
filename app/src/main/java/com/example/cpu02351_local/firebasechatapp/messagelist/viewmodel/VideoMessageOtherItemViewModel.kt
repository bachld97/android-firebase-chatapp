package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.os.Bundle
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.VideoMessageItem


class VideoMessageOtherItemViewModel(messageItem: MessageItem, private val imageClick: MessageItemAdapter.ItemClickCallback) : MessageOtherItemViewModel(messageItem) {
    fun imageClicked() {
        imageClick.onClick(messageItem)
    }

    fun getAttributeBundle(): Bundle {
        val res = Bundle()
        res.putInt("width", (messageItem as VideoMessageItem).width)
        res.putInt("height", (messageItem as VideoMessageItem).height)
        res.putString("url", messageItem.getContent())
        return res
    }
}
