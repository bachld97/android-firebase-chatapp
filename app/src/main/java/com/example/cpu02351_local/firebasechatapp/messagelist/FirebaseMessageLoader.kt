package com.example.cpu02351_local.firebasechatapp.messagelist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.BY_USERS
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.CONVERSATIONS
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.DELIM
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.LAST_MOD
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.MESSAGE
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.model.Message
import com.example.cpu02351_local.firebasechatapp.utils.DaggerFirebaseReferenceComponent
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FirebaseMessageLoader : MessageLoader {

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadInitialMessages(conversationId: String, limit: Int): Single<List<Message>> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
                .orderByKey()
                .limitToLast(limit)
        lateinit var listener : ValueEventListener
        val s = Single.create<List<Message>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val res = snapshot?.children
                            ?.map { FirebaseMessage().toMessageFromMap(it.key, it.value) }
                    if (res == null || res.isEmpty()) {
                        emitter.onError(Throwable("No message yet"))
                    } else {
                        emitter.onSuccess(res)
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    // Do nothing
                }

            }
            reference.addListenerForSingleValueEvent(listener)
        }

        return s.doFinally { reference.removeEventListener(listener) }
    }

    override fun observeNextMessages(conversationId: String, lastKey: String?): Observable<Message> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
                .startAt(lastKey)
                .orderByKey()
        lateinit var listener: ChildEventListener
        val obs = Observable.create<Message> { emitter ->
            // Subsequent loads
            listener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    // Do nothing here?
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    // Do nothing here
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    // Do nothing here
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    // Do nothing here
                }

                override fun onChildAdded(snapshot: DataSnapshot?, previousChildName: String?) {
                    val message = FirebaseMessage()
                    message.fromMap(snapshot?.key as String, snapshot.value)
                    emitter.onNext(message.toMessage())
                }
            }
            reference.addChildEventListener(listener)
        }

        return obs.doFinally { reference.removeEventListener(listener) }
    }

    override fun loadMore(conversationId: String, lastKey: String?, messageLimit: Int): Single<List<Message>> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
                .endAt(lastKey)
                .orderByKey()
                .limitToLast(messageLimit)
        lateinit var listener: ValueEventListener
        val single = Single.create<List<Message>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val res = snapshot?.children
                            ?.filter { it.key != lastKey }
                            ?.map { FirebaseMessage().toMessageFromMap(it.key, it.value) }

                    if (res != null && res.isNotEmpty()) {
                        emitter.onSuccess(res)
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Cannot load more message"))
                }
            }

            reference.addListenerForSingleValueEvent(listener)
        }
        return single.doFinally { reference.removeEventListener(listener) }
    }

    override fun loadMessages(conversationId: String, limit: Int): Observable<Message> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
                .orderByKey()
                .limitToLast(limit)
        lateinit var listener: ChildEventListener
        val obs = Observable.create<Message> { emitter ->
            // Subsequent loads
            listener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    // Do nothing here?
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                    // Do nothing here
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                    // Do nothing here
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    // Do nothing here
                }

                override fun onChildAdded(snapshot: DataSnapshot?, previousChildName: String?) {
                    val message = FirebaseMessage()
                    message.fromMap(snapshot?.key as String, snapshot.value)
                    emitter.onNext(message.toMessage())
                }
            }
            reference.addChildEventListener(listener)
        }

        return obs.doFinally { reference.removeEventListener(listener) }
    }


    override fun getNewMessageId(conversationId: String): String {
        return databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE").push().key
    }

    override fun addMessage(conversationId: String, message: Message, byUsers: List<String>): Completable {
        // Check if conversation exist
        return Completable.create { emitter ->
            val conversationRef = databaseRef.child(CONVERSATIONS)
            conversationRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    if (snapshot?.children?.map { it -> it.key }?.contains(conversationId) != true) {
                        addConversation(conversationId, byUsers, message.atTime.toString())
                    }

                     conversationRef.child("$conversationId/$MESSAGE/${message.id}")
                            .setValue(FirebaseMessage.from(message).toMap())
                            .addOnSuccessListener {
                                databaseRef.child("$CONVERSATIONS/$conversationId/$LAST_MOD")
                                        .setValue(message.atTime.toString())
                                emitter.onComplete()
                            }
                            .addOnFailureListener {
                                emitter.onError(it)
                            }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Cannot send message"))
                }
            })
        }
    }

    private fun addConversation(conversationId: String, byUsers: List<String>, lastMod: String) {
        val con = FirebaseConversation()
        val map = HashMap<String, String>()
        Log.d("DEBUGGING", byUsers.toString())
        map[LAST_MOD] = lastMod
        map[BY_USERS] = byUsers.joinToString(DELIM)
        con.fromMap(conversationId, map)
        databaseRef.child("$CONVERSATIONS/$conversationId")
                .updateChildren(con.toMap(), { error, _ ->
                    if (error == null) {
                        addConversationToUsers(byUsers, conversationId)
                    }
                })
    }

    private fun addConversationToUsers(byUsers: List<String>, conversationId: String) {
        byUsers.forEach { userId ->
            databaseRef.child("$USERS/$userId")
                    .runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData?): Transaction.Result {
                            val u = FirebaseUser()
                            u.fromMap(conversationId, mutableData?.value)
                            val temp = ArrayList<String>()
                            temp.addAll(u.conversationIds.split(DELIM))
                            temp.remove(conversationId)
                            temp.add(0, conversationId)
                            u.conversationIds = temp.joinToString(DELIM).trim()
                            mutableData?.value = u.toMap()
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                            // Do nothing for now
                        }
                    })
        }
    }
}