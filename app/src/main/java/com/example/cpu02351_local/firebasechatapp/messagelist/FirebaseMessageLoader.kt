package com.example.cpu02351_local.firebasechatapp.messagelist

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseMessage
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.CONVERSATIONS
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.MESSAGE
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.google.firebase.database.*
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseMessageLoader: MessageLoader {

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadMessages(conversationId: String): Observable<Message> {
        val reference = databaseRef.child("$CONVERSATIONS/$conversationId/$MESSAGE")
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

            reference.apply {
                addChildEventListener(listener)
            }
        }

        return obs.doFinally { reference.apply {
            removeEventListener(listener)
        } }
    }

    override fun addMessage(conversationId: String, message: Message): Completable {
        TODO()
    }
}