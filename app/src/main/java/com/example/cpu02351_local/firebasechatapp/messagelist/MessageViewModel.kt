package com.example.cpu02351_local.firebasechatapp.messagelist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
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
                // messageView.onNetworkLoadInitial(t)
                onFirstLoad(t)
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
                // messageView.onNewMessage(t)
                onMessageAdded(t)
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


    // rework this function
    fun sendNewMessage() {
        if (messageText.trim().isEmpty()) {
            return
        }
        val id = messageLoader.getNewMessageId(conversationId)
        val m = TextMessage(id, System.currentTimeMillis(), messageView.getSender(), messageText)
        m.isSending = true
        messageText = ""
        messageView.onRequestSendMessage()
        onMessageAdded(m)
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
                        // messageView.onLoadMoreResult(res)
                        onLoadMore(res)
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
                        // messageView.onLocalLoadInitial(t)
                        onFirstLoad(t)
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


    private var mMessageItems = ArrayList<MessageItem>()
    private fun onFirstLoad(data: List<AbstractMessage>) {
        mMessageItems.clear()
        mMessageItems.addAll(data.mapIndexed { index, abstractMessage ->
            val shouldDisplayTime = (index == data.size - 1) ||  (data[index + 1].byUser != abstractMessage.byUser)
            val shouldDisplaySender = (index == 0) || (data[index - 1].byUser != abstractMessage.byUser)
            val fromThisUser = abstractMessage.byUser == messageView.getSender()
            abstractMessage.toMessageItem(shouldDisplaySender, shouldDisplayTime, fromThisUser)
        })
        mMessageItems.reverse()
        dispatchInfo()
    }

    private fun onLoadMore(data: List<AbstractMessage>) {
        val tem = data.mapIndexed { index, abstractMessage ->
            val shouldDisplayTime = (index == data.size - 1) ||  (data[index + 1].byUser != abstractMessage.byUser)
            val shouldDisplaySender = (index == 0) || (data[index - 1].byUser != abstractMessage.byUser)
            val fromThisUser = abstractMessage.byUser == messageView.getSender()
            abstractMessage.toMessageItem(shouldDisplaySender, shouldDisplayTime, fromThisUser)
        }.reversed()

        // tem.first() nullable --> can crash here
        val newFirst =  tem.firstOrNull() ?: return // No more new message
        val oldLast = tem.last()
        val isSameSender = newFirst.getSenderId() == oldLast.getSenderId()
        newFirst.shouldDisplayTime = !isSameSender
        oldLast.shouldDisplaySenderInfo = !isSameSender

        mMessageItems.addAll(tem)
        dispatchInfo()
    }

    private fun onMessageAdded(addedMessage: AbstractMessage) {
        var pos = -1
        val fromThisUser = addedMessage.byUser == messageView.getSender()
        val shouldDisplaySender = mMessageItems.first().getSenderId() != addedMessage.byUser
        val curItem = if (mMessageItems.isNotEmpty()) {
            mMessageItems.first().shouldDisplayTime = false
            addedMessage.toMessageItem(shouldDisplaySender, true, fromThisUser)
        } else {
            addedMessage.toMessageItem(true, true, fromThisUser)
        }

        mMessageItems.forEachIndexed { index, messageItem ->
            if (messageItem.equalsItem(curItem)) {
                pos = index
            }
        }

        if (pos == -1)
            mMessageItems.add(0, curItem)
        else {
            curItem.shouldDisplayTime = (pos == 0) || curItem.getSenderId() != mMessageItems[pos + 1].getSenderId()
            mMessageItems[pos] = curItem
        }

        dispatchInfo()
    }

    private fun dispatchInfo() {
        messageView.updateMessageItem(mMessageItems)
    }

    private fun AbstractMessage.toMessageItem(shouldDisplaySender: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean): MessageItem {
        return convertMessageToMessageItem(this,shouldDisplaySender, shouldDisplayTime, fromThisUser)
    }

    private fun convertMessageToMessageItem(message: AbstractMessage, shouldDisplaySender: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean): MessageItem {
        return MessageItem(message, shouldDisplaySender, shouldDisplayTime, fromThisUser)
    }
}

