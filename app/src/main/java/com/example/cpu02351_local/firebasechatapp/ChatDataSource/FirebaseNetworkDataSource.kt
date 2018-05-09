package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.google.firebase.database.*
import java.util.*

class FirebaseNetworkDataSource : NetworkDataSource() {

    companion object {
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"
        const val CONVERSATION_PARTICIPANT = "participants"
        const val CONVERSATION_TIME = "at_time"
        const val CHILD_CONVERSATION_ID = "conversations"
    }






    private val mCons = ArrayList<Conversation>()
    private lateinit var mDisplayUnit : ListConversationDisplayUnit
    private val database = FirebaseDatabase.getInstance()
    private val reference = database.reference!!

    override fun loadConversationList(displayUnit: ListConversationDisplayUnit, userId: String) {
        mDisplayUnit = displayUnit
        reference.child(USERS).child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                val tem = snapshot?.child(CHILD_CONVERSATION_ID)!!.getValue(String::class.java)
                val conIds = tem?.split(" ")

                if (conIds == null) {
                    displayUnit.onFailLoadConversation("Error")
                    return
                }

                mCons.clear()
                for (id in conIds) {
                    loadConversation(id)
                }
            }

            override fun onCancelled(error: DatabaseError?) {
                mDisplayUnit.onFailLoadConversation(error?.message)
            }

        })
    }

    override fun addConversation(displayUnit: ListConversationDisplayUnit, participants: List<String>) {
        createNewConversation(participants)
    }
    override fun loadUserDetail(userId: String) : User {
        TODO()
    }

    override fun loadConversation(conversationId: String): Conversation {
        val con = Conversation(conversationId)
        reference.child(CONVERSATIONS).child(conversationId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                con.participantIds = snapshot?.child(CONVERSATION_PARTICIPANT)?.getValue(String::class.java)?.split(" " )
                con.createdTime = snapshot?.child(CONVERSATION_TIME)?.getValue(String::class.java)
                mCons.add(con)
                mDisplayUnit.onSuccessfulLoadConversations(mCons)
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing :D
            }
        })


        return con
    }

    override fun loadMessage(messageId: String): Message {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun createNewConversation(participants: List<String>): String {
        val participantString = participants.joinToString(" ")
        val uuid = UUID.randomUUID().toString()
        val newCon = HashMap<String, String>()
        newCon[CONVERSATION_PARTICIPANT] = participantString
        newCon[CONVERSATION_TIME] = System.currentTimeMillis().toString()
        val createdCon = Conversation(uuid)
        createdCon.createdTime = newCon[CONVERSATION_TIME]
        createdCon.participantIds = participants
        mDisplayUnit.addOrUpdateConversation(createdCon)
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
                val u = data?.getValue<User>(User::class.java)
                if (u != null) {
                    u.conversations = uuid + " " + u.conversations
                    data.value = u
                } else {
                }

                return Transaction.success(data)
            }

            override fun onComplete(error: DatabaseError?, b: Boolean, snapshot: DataSnapshot?) {
            }
        })
    }
}