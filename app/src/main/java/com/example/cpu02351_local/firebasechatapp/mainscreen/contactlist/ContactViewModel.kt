package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalUserDatabase
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class ContactViewModel(private val contactLoader: ContactLoader,
                       private val contactView: ContactView,
                       private val userId: String) {
    private var mDisposable: Disposable? = null
    private var localUserDatabase: LocalUserDatabase? = null

    init {
        loadContacts()
    }

    fun setLocalUserDatabase(localUserDatabase: LocalUserDatabase) {
       this.localUserDatabase = localUserDatabase
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadContacts() {
        val obs = contactLoader.loadContacts(userId)
        dispose()
        obs.subscribe(object : SingleObserver<List<User>> {
            override fun onSuccess(t: List<User>) {
                localUserDatabase?.saveAll(t)
                        ?.subscribe {
                            Log.d("DEBUGGING", "save finish")
                        }
                contactView.onContactsLoaded(t.filter { it.id != userId})
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }

        })
    }

    fun onContactClicked(context: Context, user: User) {
        val userIds = arrayOf(userId, user.id)
        val conId = Conversation.uniqueId(userIds)
        val intent = MessageListActivity.newInstance(context, conId, conId)
        context.startActivity(intent)
    }
}