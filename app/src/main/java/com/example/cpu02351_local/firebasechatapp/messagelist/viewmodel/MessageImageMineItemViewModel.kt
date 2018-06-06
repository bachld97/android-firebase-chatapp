package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.os.Bundle
import android.view.View
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage


class MessageImageMineItemViewModel(messageItem: MessageItem, var v: View, private val imageClick: MessageItemAdapter.ItemClickCallback) : MessageBaseItemViewModel(messageItem) {
    fun imageClicked() {
        imageClick.onClick(messageItem)
    }

    fun getAttributeBundle(): Bundle {
        val res = Bundle()
        res.putInt("width", (messageItem as ImageMessageItem).width)
        res.putInt("height", (messageItem as ImageMessageItem).height)


        val mess = messageItem.message
        val url = if (mess is ImageMessage) {
            if (mess.localUri.toString().isNotEmpty()) {
                mess.localUri.toString()
            } else {
                messageItem.getContent()
            }
        } else {
            messageItem.getContent()
        }

        res.putString("url", url)
        return res
    }
}
