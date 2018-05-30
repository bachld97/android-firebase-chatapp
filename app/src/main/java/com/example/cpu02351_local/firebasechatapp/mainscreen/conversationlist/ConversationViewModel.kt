package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ConversationViewModel(private val conversationLoader: ConversationLoader,
                            private val conversationView: ConversationView,
                            val userId: String) {
    private var mDisposable: Disposable? = null
    private var localDatabase: LocalDatabase? = null

    init {
        loadConversations()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    fun resume() {
        loadLocalConversation()
        loadConversations()
    }

    private fun loadConversations() {
        val obs = conversationLoader.loadConversations(userId)
        dispose()
        obs.subscribe(object : Observer<List<Conversation>> {
            override fun onComplete() {
                // Do nothing
            }

            override fun onNext(t: List<Conversation>) {
                if (mLocalDisposable != null && !mLocalDisposable!!.isDisposed) {
                    mLocalDisposable!!.dispose()
                }


                conversationView.onConversationsLoaded(t.map { toConversationItem(it)})
                localDatabase?.saveConversationAll(t)
                        ?.subscribe { Log.d("DEBUGGING", "Conversation saved") }
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }
        })
    }

    fun toConversationItem(con: Conversation): ConversationItem {
        /*
        val isRead =  if (useReadState)
            con.lastRead[userId] != null && (con.lastMessage?.id == con.lastRead[userId])
        else
            true
        */
        /*
        if (con.lastRead[userId] != null && con.lastMessage?.id == con.lastMessage?.id) {
            // false
        }*/

        return ConversationItem(con, userId)
    }

    fun onConversationClicked(conversation: ConversationItem) {
        conversationView.navigate(conversation)
    }

    fun setLocalDatabase(localDatabase: LocalDatabase) {
        this.localDatabase = localDatabase
        loadLocalConversation()
    }

    private var mLocalDisposable: Disposable ?=null
    private fun loadLocalConversation() {
         localDatabase?.loadConversationAll()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : SingleObserver<List<Conversation>> {
                    override fun onSuccess(t: List<Conversation>) {
                        conversationView.onLocalConversationsLoaded(
                                t.filter { it.participantIds.contains(userId) }
                                        .map {  toConversationItem(it) })
                    }

                    override fun onSubscribe(d: Disposable) {
                        mLocalDisposable = d
                    }

                    override fun onError(e: Throwable) {
                        // Do nothing for now
                    }
                })
    }
}
