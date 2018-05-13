package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatModel
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseObject
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser

class FirebaseChatModel : ChatModel() {

    private val mContacts = ArrayList<FirebaseUser>()
    private val mMessages = ArrayList<FirebaseMessage>()
    private val mConversations = ArrayList<FirebaseConversation>()

    override fun loadConversations(userId: String) {
        TODO()
    }

    override fun loadMessages(conversationId: String) {
        TODO()
    }

    override fun loadContact(userId: String) {
        TODO()
    }

    override fun addConversation(users: Array<User>, conversation: Conversation) {
        TODO()
    }

    override fun addMessage(currentConversation: Conversation, message: Message) {
        TODO()
    }

    override fun addContact(currentUser: User, addedContact: User) {
        TODO()
    }

    override fun notifyDataChanged() {
        mConversationObservers.forEach { it.onConversationsLoaded(mConversations.toConversations()) }
        mMessageObservers.forEach { it.onMessagesLoaded(mMessages.toMessages()) }
        mContactObservers.forEach { it.onContactsLoaded(mContacts.toContacts()) }
    }
}

private fun ArrayList<FirebaseConversation>.toConversations(): List<Conversation> {
    val res = ArrayList<Conversation>()
    this.forEach { res.add(it.toConversation()) }
    return res
}

private fun ArrayList<FirebaseMessage>.toMessages(): List<Message> {
    val res = ArrayList<Message>()
    this.forEach { res.add(it.toMessage()) }
    return res
}

private fun ArrayList<FirebaseUser>.toContacts(): List<User> {
    val res = ArrayList<User>()
    this.forEach { res.add(it.toUser()) }
    return res
}
