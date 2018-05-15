package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.net.Uri
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.CONTACTS
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.CONVERSATIONS
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.DELIM
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.LAST_MOD
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.MESSAGE
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.addIfNotContains
import com.example.cpu02351_local.firebasechatapp.addOrUpdate
import com.google.firebase.database.*

class FirebaseChatDataSource : ChatDataSource() {

    private lateinit var mCurrentUser: FirebaseUser
    private val mContacts = ArrayList<FirebaseUser>()
    private val mMessages = ArrayList<FirebaseMessage>()
    private val mConversations = ArrayList<FirebaseConversation>()
    private val database = FirebaseHelper.getFirebaseInstance()
    private val databaseRef = database.reference!!

    init {
        databaseRef.keepSynced(true)
    }

    override fun saveAvatar(userId: String, filePath: Any) {
        val uri = filePath as Uri
        FirebaseHelper.getAvatarReference(userId).putFile(uri)
            .addOnSuccessListener {
                val downloadUri = it.downloadUrl.toString()
                Log.d("DEBUGGING", "URL $downloadUri")
                databaseRef.child("$USERS/$userId")
                        .runTransaction(object : Transaction.Handler {
                            override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                                val u = FirebaseUser()
                                u.fromMap(userId, mutableData?.value)
                                u.avaUrl = downloadUri
                                mutableData?.value = u.toMap()
                                return Transaction.success(mutableData)
                            }

                            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                                // Do nothing for now
                            }

                        })
            }
    }

    override fun loadUserDetail(id: String) {
        databaseRef.child("$USERS/$id").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                mCurrentUser = FirebaseUser()
                mCurrentUser.fromMap(id, snapshot?.value)
                notifyDataChanged()
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do Nothing for now
            }
        })
    }

    override fun loadConversations(userId: String) {
        databaseRef.child("$USERS/$userId")
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                val conIds = snapshot?.child(CONVERSATIONS)!!
                        .getValue(String::class.java)?.split(DELIM)
                if (conIds == null) {
                    // Do nothing for now
                    return
                }
                conIds.filter { it.isNotEmpty() }
                        .forEach { loadConversation(it) }
            }

            override fun onCancelled(error: DatabaseError?) {
                // Do nothing for now
            }
        })
    }

    fun loadConversation(id : String) {
        databaseRef.child("$CONVERSATIONS/$id")
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
        databaseRef.child("$CONVERSATIONS/$conversationId")
                .updateChildren(firebaseConversation.toMap(), { error, _ ->
                    if (error != null) {
                        // Do nothing for now
                    } else {
                        users.forEach { addConversationToUser(it, conversationId) } }
                })
    }

    private fun addConversationToUser(user: User, id: String) {
        databaseRef.child("$USERS/${user.id}")
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
        databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
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
        databaseRef.child("$CONVERSATIONS/$currentConversation/$MESSAGE/${message.id}")
                .setValue(FirebaseMessage.from(message).toMap())
        databaseRef.child("$CONVERSATIONS/$currentConversation/$LAST_MOD")
                .setValue(message.atTime.toString())
    }


    override fun loadContacts(userId: String) {
        databaseRef.child(USERS).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                snapshot?.children?.filter { it.key != userId }
                        ?.forEach { loadContact(it.key) }
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing
            }

        })

        /*
        databaseRef.child("$USERS/$userId")

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
        */
    }

    fun loadContact(id: String) {
        databaseRef.child("$USERS/$id")
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
        mUserDetailObservers.forEach { it.onUserDetailLoaded(mCurrentUser.toUser()) }
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
