package com.example.cpu02351_local.firebasechatapp.messagelist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MessageViewModel(private val messageLoader: MessageLoader,
                       private val messageView: MessageView,
                       private val conversationId: String) {
    private var mDisposable: Disposable? = null
    init {
        loadMessages()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadMessages() {
        val obs = messageLoader.loadMessages(conversationId)
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
}