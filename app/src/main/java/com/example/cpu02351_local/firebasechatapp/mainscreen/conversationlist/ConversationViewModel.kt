package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ConversationViewModel(private val conversationLoader: ConversationLoader,
                            private val resultListener: ConversationView,
                            private val userId: String) {
    private var mDisposable: Disposable? = null

    init {
        loadConversations()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadConversations() {
        val obs = conversationLoader.loadConversations(userId)
        dispose()
        obs.subscribe(object : Observer<List<Conversation>> {

            override fun onComplete() {
                dispose()
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: List<Conversation>) {
                resultListener.onConversationsLoaded(t)
            }

            override fun onError(e: Throwable) {
            }
        })
    }
}
