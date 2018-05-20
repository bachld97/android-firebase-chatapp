package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single

interface ConversationLoader {
    fun loadConversations(userId: String): Observable<List<Conversation>>
    fun loadConversation(conversationId: String): Single<Conversation>
}