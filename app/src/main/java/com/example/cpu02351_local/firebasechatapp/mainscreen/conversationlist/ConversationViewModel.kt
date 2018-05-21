package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ConversationViewModel(private val conversationLoader: ConversationLoader,
                            private val resultListener: ConversationObserver,
                            private val userId: String) {

    init {
        loadConversations()
    }

    private fun loadConversations() {
        val obs = conversationLoader.loadConversations(userId)
        obs.subscribe(object : Observer<List<Conversation>> {
            private lateinit var disposable: Disposable

            fun dispose() {
                if (!disposable.isDisposed)
                    disposable.dispose()
            }

            override fun onComplete() {
                dispose()
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: List<Conversation>) {
                resultListener.onConversationsLoaded(t)
            }

            override fun onError(e: Throwable) {
                dispose()
            }
        })
    }
}
