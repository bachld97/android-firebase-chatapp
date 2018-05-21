package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.CONVERSATIONS
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.DELIM
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FirebaseConversationLoader : ConversationLoader {

    @Inject lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadConversations(userId: String): Observable<List<Conversation>> {
        val reference = databaseRef.child("$USERS/$userId")
        lateinit var listener: ValueEventListener
        val obs = io.reactivex.Observable.create<List<Conversation>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val conIds = snapshot?.child(CONVERSATIONS)
                            ?.getValue(String::class.java)?.split(DELIM)
                    if (conIds == null) {
                        Log.i("Info", "ConversationIds is empty")
                        return
                    }
                    val resArray = ArrayList<Conversation>()
                    conIds.filter { it.isNotEmpty() }
                            .forEach { id ->
                                loadConversation(id).subscribe(object : SingleObserver<Conversation> {
                                    private lateinit var disposable: Disposable

                                    override fun onSuccess(t: Conversation) {
                                        resArray.add(t)
                                        emitter.onNext(resArray)
                                        if (!disposable.isDisposed) disposable.dispose()
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                        disposable = d
                                    }

                                    override fun onError(e: Throwable) {
                                        emitter.onError(Throwable("Error loading conversation with id $id"))
                                    }
                                })
                            }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("New data cannot be fetched due to some errors"))
                }

            }
            reference.addValueEventListener(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener) }
    }

    override fun loadConversation(conversationId: String): Single<Conversation> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId")
        lateinit var listener: ValueEventListener
        val obs = Single.create<Conversation> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val con = FirebaseConversation()
                    con.fromMap(conversationId, snapshot?.value)
                    emitter.onSuccess(con.toConversation())
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Cannot fetch conversation information"))
                }
            }
            reference.addListenerForSingleValueEvent(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener)}
    }
}
