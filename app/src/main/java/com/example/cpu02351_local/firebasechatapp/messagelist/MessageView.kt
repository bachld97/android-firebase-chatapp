package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.Message

interface MessageView {
    fun onError()
    fun onNewMessage(message: Message)
    fun addMessage()
}