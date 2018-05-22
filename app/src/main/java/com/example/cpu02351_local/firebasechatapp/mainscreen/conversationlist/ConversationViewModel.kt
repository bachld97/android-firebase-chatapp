package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.content.Context
import android.content.Intent
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class ConversationViewModel(private val conversationLoader: ConversationLoader,
                            private val conversationView: ConversationView,
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
        obs.subscribe(object : SingleObserver<List<Conversation>> {
            override fun onSuccess(t: List<Conversation>) {
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

    fun onConversationClicked(context: Context, conversationId: String) {
        startConversation(context, conversationId)
    }

    private fun startConversation(context: Context, conversationId: String) {
        val intent = Intent(context, MessageListActivity::class.java)
        intent.putExtra(MessageListActivity.CONVERSATION_ID, conversationId)
        context.startActivity(intent)
    }
}
