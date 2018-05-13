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
import com.google.firebase.database.*

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
                con.fromMap(id, snapshot?.getValue())
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
                con.fromMap(id, snapshot?.getValue())
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
        mConversations.add(firebaseConversation)

        reference.child("$CONVERSATIONS/$id")
                .updateChildren(firebaseConversation.toMap(), { error, _ ->
                   if (error != null) {
                        // Do nothing for now
                    } else {
                       users.forEach { addConversationToUser(it, id) }
                   }
                })
    }

    private fun addConversationToUser(user: User, id: String) {
        reference.child("$USERS/${user.id}")
                .runTransaction(object : Transaction.Handler {
                    override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                        val u = FirebaseUser()
                        u.fromMap(id, mutableData?.value)
                        var temp = u.conversationIds
                        temp = ("$id $temp").trim()
                        u.conversationIds = temp
                        mutableData?.value = u.toMap()
                        return Transaction.success(mutableData)
                    }

                    override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                        // Do nothing for now
                    }

                })
    }

    override fun addMessage(currentConversation: Conversation, message: Message) {
        reference.child("$CONVERSATIONS/$currentConversation/$MESSAGE/${message.id}")
                .setValue(FirebaseMessage.from(message).toMap())
        reference.child("$CONVERSATIONS/$currentConversation/$LAST_MOD")
                .setValue(message.atTime)
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
