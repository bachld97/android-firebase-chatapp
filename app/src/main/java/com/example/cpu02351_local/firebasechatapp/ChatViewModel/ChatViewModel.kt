package com.example.cpu02351_local.firebasechatapp.ChatViewModel

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.*
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.addOrUpdateAll
import com.example.cpu02351_local.firebasechatapp.removeIfContains

class ChatViewModel(private val mChatDataSource: ChatDataSource,
                    private val loggedInUserId: String) : ConversationDataObserver, MessageDataObserver, ContactDataObserver {

    private val mConversations = ArrayList<Conversation>()
    private val mMessages = ArrayList<Message>()
    private val mContacts = ArrayList<User>()
    private val mConversationObservers: ArrayList<ConversationViewObserver> = ArrayList()
    private val mMessageObservers: ArrayList<MessageViewObserver> = ArrayList()
    private val mContactObservers: ArrayList<ContactViewObserver> = ArrayList()
    lateinit var loggedInUser: User

    fun init() {
        mChatDataSource.registerConversationObserver(this)
        mChatDataSource.registerContactObserver(this)
        mChatDataSource.registerMessageObserver(this)
        mChatDataSource.loadContacts(loggedInUserId)
        mChatDataSource.loadConversations(loggedInUserId)
        loggedInUser = User(loggedInUserId)
    }

    fun dispose() {
        mChatDataSource.unregisterContactObserver(this)
        mChatDataSource.unregisterConversationObserver(this)
        mChatDataSource.unregisterMessageObserver(this)
    }

    fun register(obs: ConversationViewObserver) {
        mConversationObservers.addIfNotContains(obs)
        notifyDataChanged()
    }

    fun register(obs: MessageViewObserver) {
        mMessageObservers.addIfNotContains(obs)
        notifyDataChanged()
    }

    fun register(obs: ContactViewObserver) {
        mContactObservers.addIfNotContains(obs)
        notifyDataChanged()
    }

    fun unregister(obs: ConversationViewObserver) {
        mConversationObservers.removeIfContains(obs)
    }

    fun unregister(obs: MessageViewObserver) {
        mMessageObservers.removeIfContains(obs)
    }

    fun unregister(obs: ContactViewObserver) {
        mContactObservers.removeIfContains(obs)
    }

    override fun onConversationsLoaded(conversations: List<Conversation>) {
        mConversations.addOrUpdateAll(conversations)
        notifyDataChanged()
    }

    override fun onMessagesLoaded(messages: List<Message>) {
        mMessages.addOrUpdateAll(messages)
        Log.d("DEBUGGING", "Empty mess? ${mMessages.size}")
        notifyDataChanged()
    }

    override fun onContactsLoaded(contacts: List<User>) {
        mContacts.addOrUpdateAll(contacts)
        notifyDataChanged()
    }

    private fun notifyDataChanged() {
        mConversationObservers.forEach { it.onConversationsLoaded(mConversations) }
        mMessageObservers.forEach { it.onMessagesLoaded(mMessages) }
        mContactObservers.forEach { it.onContactsLoaded(mContacts) }
    }

    fun loadMessages(id: String) {
        mChatDataSource.loadMessages(id)
    }

    fun sendMessage(conId: String, message: Message, byUsersString: String?) {
        val list = (byUsersString?.split(Conversation.ID_DELIM)
                ?: conId.split(Conversation.ID_DELIM)).map { id -> User(id) }
        val arr = list.toTypedArray()
        mChatDataSource.addConversation(arr, conId)
        mChatDataSource.addMessage(conId, message)
    }
}

