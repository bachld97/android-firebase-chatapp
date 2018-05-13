package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatModel
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.addOrUpdate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseChatModel : ChatModel() {

    companion object {
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"
        const val MESSAGE = "messages"
        const val CONTACTS = "contacts"
        const val PARTICIPANTS = "by_users"
        const val LAST_MOD = "last_mode"
        const val TIME = "at_time"
        const val DELIM = " "
    }

    private val mContacts = ArrayList<FirebaseUser>()
    private val mMessages = ArrayList<FirebaseMessage>()
    private val mConversations = ArrayList<FirebaseConversation>()
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference!!

    init {
        reference.keepSynced(true)
    }

    override fun loadConversations(userId: String) {
        reference.child("$USERS/$userId")
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                val conIds = snapshot?.child(CONVERSATIONS)!!
                        .getValue(String::class.java)?.split(DELIM)
                if (conIds == null) {
                    // Do nothing for now
                    return
                }
                for (id in conIds) loadConversation(id)
            }

            override fun onCancelled(error: DatabaseError?) {
                // Do nothing for now
            }

        })
    }

    fun loadConversation(id : String) {
        reference.child("$CONVERSATIONS/$id")
                .addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {
                val con = FirebaseConversation()
                con.load(id, snapshot?.getValue())
                mConversations.addOrUpdate(con)
                notifyDataChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }
        })
    }

    override fun loadMessages(conversationId: String) {
        reference.child("$CONVERSATIONS/$conversationId/$MESSAGE")
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot?.value != null) {
                    val tem = try {
                        snapshot.value as Map<String, *>

                    } catch (e: TypeCastException) {
                        Log.d("BUG_FOUND", "FirebaseChatModel::loadMessages")
                        null
                    }
                    tem?.forEach() {id, content ->
                        val msg = FirebaseMessage()
                        msg.load(id, content)
                        mMessages.addIfNotContains(msg)
                        notifyDataChanged()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }

        })
    }

    override fun loadContacts(userId: String) {
        reference.child("$USERS/$userId")
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                val conIds = snapshot?.child(CONTACTS)!!
                        .getValue(String::class.java)?.split(DELIM)
                if (conIds == null) {
                    // Do nothing for now
                    return
                }
                for (id in conIds) loadContact(id)
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }

        })
    }

    fun loadContact(id: String) {
        reference.child("$USERS/$id")
                .addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {
                val con = FirebaseUser()
                con.load(id, snapshot?.getValue())
                mContacts.addOrUpdate(con)
                notifyDataChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }

        })
    }


    override fun addConversation(users: Array<User>, conversation: Conversation) {
        val firebaseConversation = FirebaseConversation.from(conversation)
        val id = Conversation.uniqueId(users)
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
