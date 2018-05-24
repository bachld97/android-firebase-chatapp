package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.content.Context
import android.content.Intent
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.Message
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ConversationViewModel(private val conversationLoader: ConversationLoader,
                            private val conversationView: ConversationView,
                            val userId: String) {
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
                // Do nothing
            }

            override fun onNext(t: List<Conversation>) {
                conversationView.onConversationsLoaded(t)
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }
        })
    }

    fun onConversationClicked(context: Context, conversation: Conversation) {
        startConversation(context, conversation)
    }

    private fun startConversation(context: Context, conversation: Conversation) {
        val intent = MessageListActivity.newInstance(
                context, conversation.id, conversation.participantIds.joinToString(Conversation.ID_DELIM))
        context.startActivity(intent)
    }
}
