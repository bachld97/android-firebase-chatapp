package com.example.cpu02351_local.firebasechatapp.ChatCore

import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource

class ChatController(
        private val networkDataSource: NetworkDataSource) {

    // Only support load data from network for now
    fun loadConversations(displayUnit: ListConversationDisplayUnit, userId: String) {
        networkDataSource.loadConversationList(displayUnit, userId)
    }

    fun addConversation(displayUnit: ListConversationDisplayUnit, participants: List<String>) {
        networkDataSource.addConversation(displayUnit, participants)
    }
}