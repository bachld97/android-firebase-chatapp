package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

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
import io.reactivex.Single
import javax.inject.Inject

class FirebaseConversationLoader : ConversationLoader {

    @Inject lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadConversations(userId: String): Single<List<Conversation>> {
        val reference = databaseRef.child(CONVERSATIONS)
        lateinit var listener: ValueEventListener
        val obs = Single.create<List<Conversation>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val res = snapshot?.children
                            ?.map { it -> FirebaseConversation().toConversationFromMap(it.key, it.value) }
                            ?.filter { it.participantIds.contains(userId) }

                    if (res != null) {
                        emitter.onSuccess(res)
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
