package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.model.Conversation

interface ConversationView {
    fun onConversationsLoaded(result: List<Conversation>)
    fun onLocalConversationsLoaded(result: List<Conversation>)
}