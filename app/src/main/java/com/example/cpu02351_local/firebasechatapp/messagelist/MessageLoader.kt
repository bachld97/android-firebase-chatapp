package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageLoader {
    fun loadMessages(conversationId: String, limit: Int): Observable<AbstractMessage>
    fun addMessage(conversationId: String, message: AbstractMessage, byUsers: List<String>): Completable
    fun loadMore(conversationId: String, lastKey: String?, messageLimit: Int): Single<List<AbstractMessage>>

    fun getNewMessageId(conversationId: String): String
    fun loadInitialMessages(conversationId: String, limit: Int): Single<List<AbstractMessage>>
    fun observeNextMessages(conversationId: String, lastKey: String?): Observable<AbstractMessage>
}