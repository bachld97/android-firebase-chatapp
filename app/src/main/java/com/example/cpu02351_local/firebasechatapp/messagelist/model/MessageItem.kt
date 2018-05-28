package com.example.cpu02351_local.firebasechatapp.messagelist.model

import android.text.format.DateFormat
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

abstract class MessageItem(private val message: AbstractMessage) : ListItem {
    private val timestampDisplay: String = DateFormat.format("HH:mm", message.atTime).toString()
}