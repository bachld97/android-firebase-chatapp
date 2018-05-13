package com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation

interface ConversationDataObserver {
    fun onConversationsLoaded(conversations: List<Conversation>)
}