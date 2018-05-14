package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListContactDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListMessageDisplayUnit
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

class FirebaseNetworkDataSource : NetworkDataSource() {

    companion object {
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"
        const val CONVERSATION_PARTICIPANT = "participants"
        const val CONVERSATION_TIME = "at_time"
        const val CHILD_CONVERSATION_ID = "conversations"
        const val MESSAGES = "messages"
        const val CHILD_MESSAGE_ID = "messages"
        const val CHILD_CONTACT_ID = "contacts"
    }

    private val mCons = ArrayList<Conversation>()
    private val mMes = ArrayList<Message>()
    private val mContacts = ArrayList<User>()

    private lateinit var mListConversationDisplayUnit : ListConversationDisplayUnit
    private lateinit var mListMessageDisplayUnit: ListMessageDisplayUnit
    private lateinit var mListContactDisplayUnit: ListContactDisplayUnit


    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference!!

    init {
        reference.keepSynced(true)
    }


    override fun loadContacts(displayUnit: ListContactDisplayUnit, userId: String) {
        mListContactDisplayUnit = displayUnit
        reference.child("$USERS/$userId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get List of contacts
                val contactString = snapshot.child(CHILD_CONTACT_ID).getValue(String::class.java)
                val contactIds = contactString!!.split(" ")

                contactIds.forEach {
                    loadUserDetail(it)
                }

            }

            override fun onCancelled(error: DatabaseError?) {
                displayUnit.onDataError(error?.message)
            }
        })

    }

    override fun loadUserDetail(userId: String) : User {
        val u = User(userId)
        reference.child("$USERS/$userId").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {

                val userMap = snapshot!!.value as HashMap<String, String>
                val name = userMap["name"] as String
                // We only need the name, later avatar_url :D
                u.name = name
                mContacts.addIfNotExist(u)
                mListContactDisplayUnit.onDataLoaded(mContacts)
            }
            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing :D
            }
        })
        return u
    }

    override fun addUser(displayUnit: ListContactDisplayUnit, user: User) {
        TODO()
    }



    override fun loadConversationList(displayUnit: ListConversationDisplayUnit, userId: String) {
        mListConversationDisplayUnit = displayUnit
        reference.child("$USERS/$userId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                val tem = snapshot?.child(CHILD_CONVERSATION_ID)!!.getValue(String::class.java)
                val conIds = tem?.split(" ")

                if (conIds == null) {
                    displayUnit.onDataError("Error")
                    return
                }

                mCons.clear()
                for (id in conIds) {
                    loadConversation(id)
                }
            }

            override fun onCancelled(error: DatabaseError?) {
                mListConversationDisplayUnit.onDataError(error?.message)
            }

        })
    }

    override fun loadConversation(conversationId: String): Conversation {
        val con = Conversation(conversationId)
        reference.child("$CONVERSATIONS/$conversationId").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {
                con.participantIds = snapshot?.child(CONVERSATION_PARTICIPANT)?.getValue(String::class.java)?.split(" " )
                con.createdTime = snapshot?.child(CONVERSATION_TIME)?.getValue(Long::class.java)!!

                mCons.addIfNotExist(con)
                mListConversationDisplayUnit.onDataLoaded(mCons)
            }
            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing :D
            }
        })
        return con
    }
    override fun addConversation(displayUnit: ListConversationDisplayUnit, participants: List<String>) {
        createNewConversation(participants)
    }

    private fun createNewConversation(participants: List<String>): String {
        val participantString = participants.joinToString(" ")
        val uuid = UUID.randomUUID().toString()
        val newCon = HashMap<String, String>()
        newCon[CONVERSATION_PARTICIPANT] = participantString
        newCon[CONVERSATION_TIME] = System.currentTimeMillis().toString()

        val conObj = FirebaseUtils.convertToConversation(uuid, newCon)
        mCons.add(conObj)
        mListConversationDisplayUnit.onDataLoaded(mCons)

        reference.child("$CONVERSATIONS/$uuid").updateChildren(newCon as Map<String, Any>?,
                { databaseError, _ ->
                    if (databaseError != null) {
                        Log.d("add_or_update", databaseError.message)
                    } else {
                        for (participant in participants) {
                            addConversationToParticipant(uuid, participant)
                        }
                    }
                })
        return uuid
    }

    private fun addConversationToParticipant(uuid: String, participant: String) {
        reference.child("$USERS/$participant").runTransaction(object : Transaction.Handler {
            override fun doTransaction(data: MutableData?): Transaction.Result {
                Log.d("add_or_update", data.toString())

                val u = data?.value as HashMap<String, String>
                var temp = u[CHILD_CONVERSATION_ID]
                if (temp == null) temp = ""
                temp = ("$uuid $temp").trim()
                u[CHILD_CONVERSATION_ID] = temp
                data.value = u
                return Transaction.success(data)
            }

            override fun onComplete(error: DatabaseError?, b: Boolean, snapshot: DataSnapshot?) {
            }
        })
    }


    @Suppress("UNCHECKED_CAST")
    override fun loadMessageList(displayUnit: ListMessageDisplayUnit, conversationId: String) {
        mListMessageDisplayUnit = displayUnit
        reference.child("$CONVERSATIONS/$conversationId/$CHILD_MESSAGE_ID").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot?.value == null) {
                    return
                }


                val tem = snapshot.value as Map<String, *>
                tem.forEach { msgId, msgContent ->
                    val msgMap = msgContent as Map<String, *>
                    val msg = FirebaseUtils.convertToMessage(msgMap, msgId)
                    mMes.addIfNotExist(msg)
                    displayUnit.onDataLoaded(mMes)
                }

                /*
                val tem = snapshot?.child(CHILD_MESSAGE_ID)!!.getValue(String::class.java)
                val mesIds = tem?.split(" ")

                if (mesIds == null) {
                    displayUnit.onDataError("Empty")
                    return
                }

                mCons.clear()
                for (id in mesIds) {
                    loadMessage(id)
                }
                */
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Err
            }
        })
    }

    override fun loadMessage(messageId: String): Message {
        val mes = Message(messageId)
        reference.child("$MESSAGES/$messageId").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {
                // con.participantIds = snapshot?.child(CONVERSATION_PARTICIPANT)?.getValue(String::class.java)?.split(" " )
                // con.createdTime = snapshot?.child(CONVERSATION_TIME)?.getValue(String::class.java)

                if (!mMes.contains(mes)) {
                    mMes.add(mes)
                } else {
                    val pos = mMes.indexOf(mes)
                    mMes[pos] = mes
                }
                 mListMessageDisplayUnit.onDataLoaded(mMes)
            }
            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing :D
            }
        })
        return mes
    }

    override fun addMessage(displayUnit: ListMessageDisplayUnit, conversationId: String, newMess: Message) {
        reference.child("$CONVERSATIONS/$conversationId/$MESSAGES/${newMess.id}")
                .setValue(FirebaseUtils.convertToMessageMap(newMess))
        reference.child("$CONVERSATIONS/$conversationId/at_time").setValue(newMess.atTime)
    }


    override fun dispose() {

    }


    private fun <T> ArrayList<T>.addIfNotExist(item: T) {
        if (!this.contains(item)) {
            this.add(item)
        } else {
            val pos = this.indexOf(item)
            this[pos] = item
        }
    }
}

