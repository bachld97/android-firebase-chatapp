package com.example.cpu02351_local.firebasechatapp.messagelist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.messagetypes.TextMessage
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class MessageViewModel(private val messageLoader: MessageLoader,
                       private val messageView: MessageView,
                       private val conversationId: String) {
    var messageText =""

    private val messageLimit = 15

    private var mDisposable: Disposable? = null
    init {
        loadMessages()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    var mLastKey: String? = null

    private fun loadMessages() {
        val single = messageLoader.loadInitialMessages(conversationId, messageLimit)
        dispose()
        single.subscribe(object : SingleObserver<List<AbstractMessage>> {
            override fun onSuccess(t: List<AbstractMessage>) {
                if (mLocalDisposable != null && !mLocalDisposable!!.isDisposed) {
                    mLocalDisposable!!.dispose()
                }
                messageView.onNetworkLoadInitial(t)

                mLocalDatabase?.saveMessageAll(t, conversationId)
                        ?.subscribe { Log.d("DEBUGGING", "Message saved for conversation $conversationId") }

                mLastKey = t.first().id
                observeNextMessage(t.last().id)
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                observeNextMessage(null)
            }
        })
    }

    fun observeNextMessage(lastKey: String?) {
        val obs = messageLoader
                .observeNextMessages(conversationId, lastKey, messageView.getSender())
        dispose()
        obs.subscribe(object : Observer<AbstractMessage> {
            override fun onComplete() {
                dispose()
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onNext(t: AbstractMessage) {
                messageView.onNewMessage(t)
            }

            override fun onError(e: Throwable) {
                // Do nothing
            }
        })
    }

    private fun proceedSendMessage(message: AbstractMessage, participantIds: String) {
        val list = participantIds.split(Conversation.ID_DELIM)
        val com = messageLoader.addMessage(conversationId, message, list)
        com.subscribe(object : CompletableObserver {
            override fun onComplete() {
                // messageView.onNewMessage(message)
                updatePendingMessageState(message)
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                messageView.onError()
            }
        })
    }

    fun sendNewMessage() {
        if (messageText.trim().isEmpty()) {
            return
        }
        val id = messageLoader.getNewMessageId(conversationId)
        val m = TextMessage(id, System.currentTimeMillis(), messageView.getSender(), messageText)
        m.isSending = true
        messageText = ""
        messageView.onRequestSendMessage(m)
        addPendingMessage(m)
        proceedSendMessage(m, messageView.getParticipants())
    }

    private fun updatePendingMessageState(message: AbstractMessage) {
        message.isSending = false
        mLocalDatabase?.saveMessageAll(arrayListOf(message), conversationId)
                ?.subscribe { Log.d("DEBUGGING", "New message sent") }
    }

    private fun addPendingMessage(message: AbstractMessage) {
        mLocalDatabase?.updateConversationLastMessage(conversationId, message.id)
                ?.subscribe { Log.d("DEBUGGING", "Last message Id updated") }

        mLocalDatabase?.saveMessageAll(arrayListOf(message), conversationId)
                ?.subscribe { Log.d("DEBUGGING", "New message sent") }
    }

    fun loadMore() {
        messageLoader.loadMore(conversationId, mLastKey, messageLimit)
                .subscribe({ res ->
                        messageView.onLoadMoreResult(res)
                        mLastKey = res.first().id
                }, { messageView.onError() })
    }

    private var mLocalDisposable: Disposable? = null
    private var mLocalDatabase: LocalDatabase? = null
    fun setLocalDatabase(localDatabase: LocalDatabase) {
        mLocalDatabase = localDatabase
        localDatabase.loadMessages(conversationId)
                .subscribe(object : SingleObserver<List<AbstractMessage>> {
                    override fun onSuccess(t: List<AbstractMessage>) {
                        messageView.onLocalLoadInitial(t)
                        t.filter { it.isSending }.forEach { proceedSendMessage(it, messageView.getParticipants()) }
                    }

                    override fun onSubscribe(d: Disposable) {
                        mLocalDisposable = d
                    }

                    override fun onError(e: Throwable) {
                        // Do nothing
                    }
                })
    }

}