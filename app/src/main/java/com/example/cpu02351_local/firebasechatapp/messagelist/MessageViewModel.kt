package com.example.cpu02351_local.firebasechatapp.messagelist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.Message
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MessageViewModel(private val messageLoader: MessageLoader,
                       private val messageView: MessageView,
                       private val conversationId: String) {
    var messageText =""

    private val messageLimit = 10
    var currentNumMessage = 0

    private var mDisposable: Disposable? = null
    init {
        loadMessages()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    fun loadMessages() {
        val obs = messageLoader
                .loadMessages(conversationId, currentNumMessage + messageLimit)
        currentNumMessage += messageLimit
        Log.d("LIMIT", "$currentNumMessage $messageLimit")
        dispose()
        obs.subscribe(object : Observer<Message> {
            override fun onComplete() {
                dispose()
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: Message) {
                messageView.onNewMessage(t)
            }

            override fun onError(e: Throwable) {
                // Do nothing
            }
        })
    }

    fun sendMessage(conId: String, message: Message, byUsers: String) {
        val list = byUsers.split(Conversation.ID_DELIM)
        val com = messageLoader.addMessage(conId, message, list)
        com.subscribe(object : CompletableObserver {
            override fun onComplete() {
                // messageView.onNewMessage(message)
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                messageView.onError()
            }

        })
    }

    fun sendNewMessage() {
        messageView.addMessage()
    }

}