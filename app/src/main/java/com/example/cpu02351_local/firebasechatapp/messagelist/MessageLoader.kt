package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.Message
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageLoader {
    fun loadMessages(conversationId: String, limit: Int): Observable<Message>
    fun addMessage(conversationId: String, message: Message, byUsers: List<String>): Completable
    fun loadMore(conversationId: String, lastKey: String?, messageLimit: Int): Single<List<Message>>

    fun getNewMessageId(conversationId: String): String
    fun loadInitialMessages(conversationId: String, limit: Int): Single<List<Message>>
    fun observeNextMessages(conversationId: String, lastKey: String?): Observable<Message>
}