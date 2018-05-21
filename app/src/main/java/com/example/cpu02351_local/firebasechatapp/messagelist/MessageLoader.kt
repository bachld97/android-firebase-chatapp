package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import io.reactivex.Completable
import io.reactivex.Observable

interface MessageLoader {
    fun loadMessages(conversationId: String): Observable<Message>
    fun addMessage(conversationId: String, message: Message): Completable
}