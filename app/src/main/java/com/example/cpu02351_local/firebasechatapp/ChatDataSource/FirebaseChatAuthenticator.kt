package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.PASSWORD
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.ChatAuthenticator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

class FirebaseChatAuthenticator : ChatAuthenticator() {
    private val database = FirebaseHelper.getFirebaseInstance()
    private val databaseRef = database.reference!!

    override fun signUp(username: String, password: String): Single<String> {
        return Single.create { emitter ->
            databaseRef.child(USERS).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    if (snapshot!!.hasChild(username)) {
                        emitter.onError(Throwable("Username already exist"))
                        return
                    }
                    emitter.onSuccess(username)
                    val u = FirebaseUser(username, password)
                    snapshot.ref.child(username).setValue(u.toMap())
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Network error, please try again later"))
                }
            })
        }
    }

    override fun signIn(username: String, password: String): Single<String> {
        return Single.create { emitter ->
            databaseRef.child("$USERS/$username").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    if (snapshot?.value != null) {
                        if (snapshot.child(PASSWORD).value == password) {
                            emitter.onSuccess(username)
                            return
                        }
                    }
                    emitter.onError(Throwable("Invalid log in information"))
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Network error, please try again later"))
                }
            })
        }
    }
}