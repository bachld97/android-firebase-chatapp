package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.content.Context
import android.content.Intent
import com.example.cpu02351_local.firebasechatapp.ChatView.MessageList.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class ContactViewModel(private val contactLoader: ContactLoader,
                       private val contactView: ContactView,
                       private val userId: String) {
    private var mDisposable: Disposable? = null

    init {
        loadContacts()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadContacts() {
        val obs = contactLoader.loadContacts(userId)
        dispose()
        obs.subscribe(object : Observer<List<User>> {
            override fun onComplete() {
                dispose()
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: List<User>) {
                contactView.onContactsLoaded(t)
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }

        })
    }

    fun onContactClicked(context: Context, user: User) {
        val intent = Intent(context, MessageListActivity::class.java)
        val userIds = arrayOf(userId, user.id)
        intent.putExtra(MessageListActivity.CONVERSATION_ID, Conversation.uniqueId(userIds))
        context.startActivity(intent)
    }
}