package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import java.io.File

interface MessageView {
    fun onError()
    fun onRequestSendMessage()
    fun getSender(): String
    fun getParticipants(): String
    fun updateMessageItem(messages: List<MessageItem>)
    fun getImageToSend(messageId: String)
    fun createImageFile(messageId: String): File?
    fun testPath(path: String)
}