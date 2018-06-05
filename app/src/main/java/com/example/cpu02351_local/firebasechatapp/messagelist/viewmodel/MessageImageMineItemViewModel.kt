package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.os.Bundle
import android.view.View
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem


class MessageImageMineItemViewModel(messageItem: MessageItem, var v: View, private val imageClick: MessageItemAdapter.ItemClickCallback) : MessageBaseItemViewModel(messageItem) {
    fun imageClicked() {
        imageClick.onClick(messageItem)
    }

    fun getAttributeBundle(): Bundle {
        val res = Bundle()
        res.putInt("width", (messageItem as ImageMessageItem).width)
        res.putInt("height", (messageItem as ImageMessageItem).height)
        res.putString("url", messageItem.getContent())
        return res
    }
}
