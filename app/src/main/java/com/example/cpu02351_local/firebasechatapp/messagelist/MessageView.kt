package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message

interface MessageView {
    fun onNewMessage(message: Message)
}