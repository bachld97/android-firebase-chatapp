package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

interface ConversationView {
    fun onConversationsLoaded(result: List<ConversationItem>)
    fun onLocalConversationsLoaded(result: List<ConversationItem>)
    fun navigate(where: ConversationItem)
}