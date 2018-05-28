package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

interface MessageView {
    fun onError()

    fun onNewMessage(message: AbstractMessage)
    fun onLoadMoreResult(moreMessages: List<AbstractMessage>)
    fun onRequestSendMessage(message: AbstractMessage)
    fun onMessageSent(message: AbstractMessage)
    fun getSender(): String
    fun getParticipants(): String

    fun onNetworkLoadInitial(messages: List<AbstractMessage>)
    fun onLocalLoadInitial(messages: List<AbstractMessage>)
}