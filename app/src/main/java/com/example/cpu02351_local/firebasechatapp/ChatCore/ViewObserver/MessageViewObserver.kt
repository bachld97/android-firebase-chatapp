package com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message

interface MessageViewObserver {
    fun onMessagesLoaded(messages: List<Message>)
}