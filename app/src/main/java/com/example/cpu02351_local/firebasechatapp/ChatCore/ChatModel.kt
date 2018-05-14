package com.example.cpu02351_local.firebasechatapp.ChatCore

import com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver.ContactDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver.ConversationDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver.MessageDataObserver
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.removeIfContains

abstract class ChatModel {
    protected val mConversationObservers: ArrayList<ConversationDataObserver> = ArrayList()
    protected val mMessageObservers: ArrayList<MessageDataObserver> = ArrayList()
    protected val mContactObservers: ArrayList<ContactDataObserver> = ArrayList()

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

    abstract fun loadConversations(userId: String)
    abstract fun loadMessages(conversationId: String)
    abstract fun loadContacts(userId: String)

    abstract fun addConversation(users: Array<User>, conversation: Conversation)
    abstract fun addMessage(currentConversation: Conversation, message: Message)
    abstract fun addContact(currentUser: User, addedContact: User)


    abstract fun notifyDataChanged()
}

