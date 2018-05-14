package com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation

interface ConversationDataObserver {
    fun onConversationsLoaded(conversations: List<Conversation>)
}