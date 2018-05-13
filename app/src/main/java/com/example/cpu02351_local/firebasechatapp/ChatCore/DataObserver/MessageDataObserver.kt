package com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message

interface MessageDataObserver {
    fun onMessagesLoaded(messages: List<Message>)
}