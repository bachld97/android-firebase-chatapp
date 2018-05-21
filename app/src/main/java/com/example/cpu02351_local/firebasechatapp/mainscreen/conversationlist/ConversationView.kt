package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation

interface ConversationView {
    fun onConversationsLoaded(result: List<Conversation>)
}