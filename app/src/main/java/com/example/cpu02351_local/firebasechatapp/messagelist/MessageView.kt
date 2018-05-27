package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.Message

interface MessageView {
    fun onError()

    fun onNewMessage(message: Message)
    fun onLoadMoreResult(moreMessages: List<Message>)
    fun onMessageSent(message: Message)
    fun addMessage()


    fun onNetworkLoadInitial(messages: List<Message>)
    fun onLocalLoadInitial(messages: List<Message>)
}