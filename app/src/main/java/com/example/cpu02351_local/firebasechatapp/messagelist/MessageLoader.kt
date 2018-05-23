package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.Message
import io.reactivex.Completable
import io.reactivex.Observable

interface MessageLoader {
    fun loadMessages(conversationId: String, limit: Int): Observable<Message>
    fun addMessage(conversationId: String, message: Message, byUsers: List<String>): Completable
}