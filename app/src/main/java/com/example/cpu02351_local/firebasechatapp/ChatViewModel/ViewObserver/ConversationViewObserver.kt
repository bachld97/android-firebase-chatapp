package com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation

interface ConversationViewObserver {
    fun onConversationsLoaded(conversations: List<Conversation>)
}