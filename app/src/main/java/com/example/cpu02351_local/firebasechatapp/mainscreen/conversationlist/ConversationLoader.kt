package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.model.Conversation
import io.reactivex.Observable

interface ConversationLoader {
    fun loadConversations(userId: String): Observable<List<Conversation>>
}