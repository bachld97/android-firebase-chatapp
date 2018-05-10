package com.example.cpu02351_local.firebasechatapp.ChatCore

import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListMessageDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource

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
}