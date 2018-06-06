package com.example.cpu02351_local.firebasechatapp.messagelist


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.TextMessage
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class MessageViewModel(private val messageLoader: MessageLoader,
                       private val messageView: MessageView,
                       private val conversationId: String) {
    var messageText =""

    private val messageLimit = 30

    private var mDisposable: Disposable? = null
    init {
        loadMessages()
    }

    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private var mObserveFromHere: String? = null
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
                        ?.subscribe {
                            // Subscribe so it can run
                        }

                mLastKey = t.first().id
                mObserveFromHere = t.last().id
                observeNextMessage(mObserveFromHere)
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
        if (message is ImageMessage && message.byUser == messageView.getSender()) {
            onMessageAdded(message)
            return
        }

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
    fun sendNewTextMessage() {
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

    fun initSendImage() {
        val messageId = messageLoader.getNewMessageId(conversationId)
        messageView.getImageToSend(messageId)
    }

    private fun updatePendingMessageState(message: AbstractMessage) {
        message.isSending = false
        mLocalDatabase?.saveMessageAll(arrayListOf(message), conversationId)
                ?.subscribe { Log.d("DEBUGGING", "New message sent") }
    }

    private fun addPendingMessage(message: AbstractMessage) {
        mLocalDatabase?.updateConversationLastMessage(conversationId, message.id)
                ?.subscribe {  }
        mLocalDatabase?.saveMessageAll(arrayListOf(message), conversationId)
                ?.subscribe {  }
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
        val failedMessaged = mMessageItems.filter { it.isFailed && it.fromThisUser }
        val newData = ArrayList<AbstractMessage>()
        newData.addAll(failedMessaged.map { it.message })
        newData.addAll(data)
        newData.filter { it.byUser == messageView.getSender() }
        newData.sortedBy { item -> item.atTime }
        mMessageItems.clear()

        mMessageItems.addAll(newData.mapIndexed { index, abstractMessage ->
            val shouldDisplayTime = (index == newData.size - 1) ||  (newData[index + 1].byUser != abstractMessage.byUser)
            val shouldDisplaySender = (index == 0) || (newData[index - 1].byUser != abstractMessage.byUser)
            val fromThisUser = abstractMessage.byUser == messageView.getSender()
            abstractMessage.toMessageItem(shouldDisplaySender, shouldDisplayTime, fromThisUser)
        })
        //mMessageItems.reverse()
        dispatchInfo()
    }


    fun retrySendImage(path: String, messageId: String) {
        // Get messageId, go look up the file
        // Take uri, then retry
        if (path.length <= 7) return
        val bitmap = BitmapFactory.decodeFile(path.substring(7)) ?: return
        resizeIfNeeded(bitmap)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ b, _ -> sendImageMessageWithBitmap(b, messageId) })
    }


    private val imageMaxSize = 1000
    private fun resizeIfNeeded(b: Bitmap): Single<Bitmap> {
        return Single.create {
            val w = b.width
            val h = b.height

            val res = if (w > h) {
                val newW = minOf(imageMaxSize, w)
                val newH = newW * h / w
                Bitmap.createScaledBitmap(b, newW,newH, false)
            } else {
                val newH = minOf(imageMaxSize, h)
                val newW = newH * w / h
                Bitmap.createScaledBitmap(b, newW,newH, false)
            }
            it.onSuccess(res)
        }
    }

    private fun sendImageMessageWithBitmap(bitmap: Bitmap, messageId: String) {
        val dimen = Pair(bitmap.width, bitmap.height)
        val imageMessage = ImageMessage(messageId, System.currentTimeMillis(), messageView.getSender(), "")
        imageMessage.width = dimen.first
        imageMessage.height = dimen.second
        imageMessage.isSending = true
        imageMessage.bitmap = bitmap
        onMessageAdded(imageMessage)
        addPendingMessage(imageMessage)

        val participantIds = messageView.getParticipants()
        val list = participantIds.split(Conversation.ID_DELIM)

        messageLoader.uploadImageBitmap(bitmap, imageMessage, conversationId, list)
                .subscribeOn(Schedulers.computation()) // IO() ?
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { /* Do nothing */ }
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

    private var firstLoad = true
    private fun onMessageAdded(addedMessage: AbstractMessage) {
        if (mObserveFromHere == null) {
            firstLoad = false
        }

        if (firstLoad) {
            firstLoad = false
            return
        }
        if (mObserveFromHere != null && addedMessage.id == mObserveFromHere) {
            return
        }

        var pos = -1
        val fromThisUser = addedMessage.byUser == messageView.getSender()
        val curItem = if (mMessageItems.isNotEmpty()) {
            mMessageItems.first().shouldDisplayTime = false
            val shouldDisplaySender =
                        mMessageItems.first().getSenderId() != addedMessage.byUser
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
            curItem.shouldDisplayTime = (pos == 0) ||
                    curItem.getSenderId() != mMessageItems[pos - 1].getSenderId()
            val updatedItem = curItem.diff(mMessageItems[pos])
            updatedItem.isSending = false
            mMessageItems[pos] = updatedItem
        }

        dispatchInfo()
    }

    private fun dispatchInfo() {
        mMessageItems.sortByDescending { messageItem -> messageItem.messTime }
        messageView.updateMessageItem(mMessageItems)
    }

    private fun AbstractMessage.toMessageItem(shouldDisplaySender: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean): MessageItem {
        return convertMessageToMessageItem(this,shouldDisplaySender, shouldDisplayTime, fromThisUser)
    }

    private fun convertMessageToMessageItem(message: AbstractMessage, shouldDisplaySender: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean): MessageItem {
        return when (message) {
            is TextMessage ->
                MessageItem(message, shouldDisplaySender, shouldDisplayTime, fromThisUser)
            is ImageMessage ->
                ImageMessageItem(message, shouldDisplaySender, shouldDisplayTime, fromThisUser)
            else -> throw RuntimeException("Unsupported type")
        }
    }

    fun sendImageMessageWithUri(fromFile: Uri?, messageId: String) {
        if (fromFile == null)
            return

        val bitmap = BitmapFactory.decodeFile(fromFile.toString().substring(7)) ?: return

        resizeIfNeeded(bitmap)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ b, _ -> sendImageMessageWithBitmap(b, messageId) })
    }

    fun resume() {
        observeNextMessage(mObserveFromHere)
    }

}

