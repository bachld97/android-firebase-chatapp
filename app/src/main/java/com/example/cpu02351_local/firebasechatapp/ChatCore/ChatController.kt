package com.example.cpu02351_local.firebasechatapp.ChatCore

import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListContactDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListMessageDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import java.util.*

class ChatController(
        private val networkDataSource: NetworkDataSource) {

    fun loadConversations(displayUnit: ListConversationDisplayUnit, userId: String) {
        networkDataSource.loadConversationList(displayUnit, userId)
    }

    fun addConversation(displayUnit: ListConversationDisplayUnit, participants: List<String>) {
        networkDataSource.addConversation(displayUnit, participants)
    }

    fun loadMessages(displayUnit: ListMessageDisplayUnit, mConversationId: String) {
        networkDataSource.loadMessageList(displayUnit, mConversationId)
    }

    fun dispose() {
        networkDataSource.dispose()
    }

    fun addMessage(displayUnit: ListMessageDisplayUnit, conversationId: String, user: String, type: String, content: String, atTime: Long = -1) {
        // switch type?
        val uuid = UUID.randomUUID().toString()
        val newMess = Message(uuid)
        if (atTime != -1L) {
            newMess.atTime = atTime
        }
        
        newMess.byUser = user
        newMess.content = content
        networkDataSource.addMessage(displayUnit, conversationId, newMess)
    }


    fun loadContact(displayUnit: ListContactDisplayUnit, currentUser: String) {
        networkDataSource.loadContacts(displayUnit, currentUser)
    }
}