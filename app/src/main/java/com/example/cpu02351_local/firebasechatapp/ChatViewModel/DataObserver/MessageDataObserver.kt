package com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message

interface MessageDataObserver {
    fun onMessagesLoaded(messages: List<Message>)
}