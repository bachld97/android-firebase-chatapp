package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.addOrUpdate
import com.google.firebase.database.*

class FirebaseChatDataSource : ChatDataSource() {

    companion object {
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"
        const val MESSAGE = "messages"
        const val CONTACTS = "contacts"
        const val BY_USER = "by_user"
        const val BY_USERS = "by_users"
        const val LAST_MOD = "last_mod"
        const val TIME = "at_time"
        const val DELIM = " "
        const val USERNAME = "name"
        const val TYPE = "type"
        const val CONTENT = "content"
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
                Log.d("DEBUGGING", conIds.toString())
                for (id in conIds) {
                    Log.d("DEBUGGING", id)
                    loadConversation(id)
                }
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
                con.fromMap(id, snapshot?.value)
                mConversations.addOrUpdate(con)
                notifyDataChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
                Log.d("DEBUGGING", "Cannot load Conversation $id")
            }
        })
    }

    override fun addConversation(users: Array<User>, conversationId: String) {
        val firebaseConversation = FirebaseConversation.from(Conversation(conversationId, users))
        reference.child("$CONVERSATIONS/$conversationId")
                .updateChildren(firebaseConversation.toMap(), { error, _ ->
                    if (error != null) {
                        // Do nothing for now
                    } else {
                        users.forEach { addConversationToUser(it, conversationId) } }
                })
    }

    private fun addConversationToUser(user: User, id: String) {
        reference.child("$USERS/${user.id}")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                        val u = FirebaseUser()
                        u.fromMap(id, mutableData?.value)
                        val temp = ArrayList<String>()
                        temp.addAll(u.conversationIds.split(DELIM))
                        temp.remove(id)
                        temp.add(0, id)
                        u.conversationIds = temp.joinToString(DELIM).trim()
                        mutableData?.value = u.toMap()
                        return Transaction.success(mutableData)
                    }

                    override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
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
                        snapshot.value as Map<String, Any>
                    } catch (e: TypeCastException) {
                        null
                    }
                    tem?.forEach {id, content ->
                        val msg = FirebaseMessage()
                        msg.fromMap(id, content)
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

    override fun addMessage(currentConversation: String, message: Message) {
        reference.child("$CONVERSATIONS/$currentConversation/$MESSAGE/${message.id}")
                .setValue(FirebaseMessage.from(message).toMap())
        reference.child("$CONVERSATIONS/$currentConversation/$LAST_MOD")
                .setValue(message.atTime.toString())
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
                con.fromMap(id, snapshot?.value)
                mContacts.addOrUpdate(con)
                notifyDataChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }

        })
    }

    override fun addContact(currentUser: String, contactId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
