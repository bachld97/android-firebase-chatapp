package com.example.cpu02351_local.firebasechatapp.messagelist

import android.graphics.Bitmap
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.AudioMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageLoader {
    fun loadMessages(conversationId: String, limit: Int): Observable<AbstractMessage>
    fun addMessage(conversationId: String, message: AbstractMessage, byUsers: List<String>): Completable
    fun loadMore(conversationId: String, lastKey: String?, messageLimit: Int): Single<List<AbstractMessage>>

    fun getNewMessageId(conversationId: String): String
    fun loadInitialMessages(conversationId: String, limit: Int): Single<List<AbstractMessage>>
    fun observeNextMessages(conversationId: String, lastKey: String?, thisUser: String): Observable<AbstractMessage>

    fun uploadImageAndUpdateDatabase(imageMessage: ImageMessage, conversationId: String, byUsers: List<String>)
    fun uploadImageBitmap(b: Bitmap, imageMessage: ImageMessage, conversationId: String, byUsers: List<String>) : Completable

    fun addAudioMessage(conversationId: String, audioMessage: AudioMessage, byUsers: List<String>): Completable
}