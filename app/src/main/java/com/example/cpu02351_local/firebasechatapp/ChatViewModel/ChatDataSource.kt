package com.example.cpu02351_local.firebasechatapp.ChatViewModel

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.DataObserver.UserDetailDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.ContactDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.ConversationDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.MessageDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.removeIfContains

abstract class ChatDataSource {
    protected val mConversationObservers: ArrayList<ConversationDataObserver> = ArrayList()
    protected val mMessageObservers: ArrayList<MessageDataObserver> = ArrayList()
    protected val mContactObservers: ArrayList<ContactDataObserver> = ArrayList()
    protected val mUserDetailObservers: ArrayList<UserDetailDataObserver> = ArrayList()

    fun registerUserDetailObserver(obs: UserDetailDataObserver) {
        mUserDetailObservers.addIfNotContains(obs)
    }

    fun registerConversationObserver(obs: ConversationDataObserver) {
        mConversationObservers.addIfNotContains(obs)
    }

    fun registerMessageObserver(obs: MessageDataObserver) {
        mMessageObservers.addIfNotContains(obs)
    }

    fun registerContactObserver(obs: ContactDataObserver) {
        mContactObservers.addIfNotContains(obs)
    }

    fun unregisterConversationObserver(obs: ConversationDataObserver) {
        mConversationObservers.removeIfContains(obs)
    }

    fun unregisterMessageObserver(obs: MessageDataObserver) {
        mMessageObservers.removeIfContains(obs)
    }

    fun unregisterContactObserver(obs: ContactDataObserver) {
        mContactObservers.removeIfContains(obs)
    }

    fun unregisterUserDetailObserver(obs: UserDetailDataObserver) {
        mUserDetailObservers.removeIfContains(obs)
    }

    abstract fun dispose()

    abstract fun loadUserDetail(id: String)
    abstract fun loadConversations(userId: String)
    abstract fun loadMessages(conversationId: String)
    abstract fun loadContacts(userId: String)

    abstract fun addConversation(users: Array<User>, conversationId: String)
    abstract fun addMessage(currentConversation: String, message: Message)
    abstract fun addContact(currentUser: String, contactId: String)


    abstract fun notifyDataChanged()
    abstract fun saveAvatar(userId: String, filePath: Any)
}

