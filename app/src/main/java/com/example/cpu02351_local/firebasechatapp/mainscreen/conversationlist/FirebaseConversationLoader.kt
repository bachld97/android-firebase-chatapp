package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseConversation
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.CONVERSATIONS
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.utils.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.MESSAGE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseConversationLoader : ConversationLoader {

    @Inject lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadConversations(userId: String): Observable<List<Conversation>> {
        val reference = databaseRef.child(CONVERSATIONS)
        lateinit var listener: ValueEventListener
        val obs = Observable.create<List<Conversation>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val res = snapshot?.children
                            ?.mapNotNull { it ->
                                FirebaseConversation().parseLastMess(snapshot.child("${it.key}/$MESSAGE").children.lastOrNull())
                                                ?.toConversationFromMap(it.key, it.value, userId)
                            }
                            ?.filter { it.participantIds.contains(userId) }

                    if (res != null) {
                        emitter.onNext(res.sortedByDescending { it.createdTime })
                    } else {
                        emitter.onError(Throwable("Cannot load conversations"))
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Cannot load conversations"))
                }
            }
            reference.addValueEventListener(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener) }
    }

}
