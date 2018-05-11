package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User

interface NetworkDataSource {
    fun loadContacts(displayUnit: ListContactDisplayUnit, userId: String)
    fun loadUserDetail(userId: String): User
    fun addUser(displayUnit: ListContactDisplayUnit, user: User)

    fun loadConversationList(displayUnit: ListConversationDisplayUnit, userId: String)
    fun loadConversation(conversationId: String): Conversation
    fun addConversation(displayUnit: ListConversationDisplayUnit, participants: List<String>)

    fun loadMessageList(displayUnit: ListMessageDisplayUnit, conversationId: String)
    fun loadMessage(messageId: String): Message
    fun addMessage(displayUnit: ListMessageDisplayUnit, conversationId: String, newMess: Message)

    fun dispose()
}