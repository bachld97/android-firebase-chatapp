package com.example.cpu02351_local.firebasechatapp.ChatCore

import com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver.*
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.addOrUpdateAll
import com.example.cpu02351_local.firebasechatapp.removeIfContains

class ChatViewModel(private val chatModel: ChatModel) : ConversationDataObserver, MessageDataObserver, ContactDataObserver {

    private val mConversations = ArrayList<Conversation>()
    private val mMessages = ArrayList<Message>()
    private val mContacts = ArrayList<User>()
    private val mConversationObservers: ArrayList<ConversationViewObserver> = ArrayList()
    private val mMessageObservers: ArrayList<MessageViewObserver> = ArrayList()
    private val mContactObservers: ArrayList<ContactViewObserver> = ArrayList()

    fun register(obs: ConversationViewObserver) {
        mConversationObservers.addIfNotContains(obs)
    }

    fun register(obs: MessageViewObserver) {
        mMessageObservers.addIfNotContains(obs)
    }

    fun register(obs: ContactViewObserver) {
        mContactObservers.addIfNotContains(obs)
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
}

