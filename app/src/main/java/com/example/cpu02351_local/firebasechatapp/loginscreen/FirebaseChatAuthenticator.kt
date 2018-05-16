package com.example.cpu02351_local.firebasechatapp.loginscreen

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.PASSWORD
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.SingleEmitter

class FirebaseChatAuthenticator : ChatAuthenticator() {
    private val database = FirebaseHelper.getFirebaseInstance()
    private val databaseRef = database.reference!!

    private val eventReferences = ArrayList<DatabaseReference>()
    private val eventListeners = ArrayList<ValueEventListener>()

    override fun dispose() {
        eventReferences.forEachIndexed { index, databaseReference ->
            databaseReference.removeEventListener(eventListeners[index])
        }
        eventListeners.clear()
        eventReferences.clear()
    }

    override fun signUp(username: String, password: String): Single<String> {
        val reference = databaseRef.child(USERS)
        lateinit var listener: ValueEventListener
        return Single.create<String> {
            emitter -> listener = firebaseSignUp(emitter, reference, username, password)
        }.doFinally {
            Log.d("DEBUGGING", "Disposed!")
            reference.removeEventListener(listener)
        }
    }

    fun firebaseSignUp(emitter: SingleEmitter<String>, reference: DatabaseReference,
                       username: String, password: String) : ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot!!.hasChild(username)) {
                    emitter.onError(Throwable("Username already exist"))
                    return
                }
                emitter.onSuccess(username)
                val u = FirebaseUser(username, password)
                snapshot.ref.child(username).setValue(u.toMap())
            }

            override fun onCancelled(error: DatabaseError?) {
                emitter.onError(Throwable("Network error, please try again later"))
            }
        }
        reference.addListenerForSingleValueEvent(listener)
        return listener
    }

    fun firebaseSignIn(emitter: SingleEmitter<String>, username: String, password: String) {
        val listener = object : ValueEventListener {
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
        }
        val reference = databaseRef.child(USERS)
        eventListeners.add(listener)
        eventReferences.add(reference)
        reference.addListenerForSingleValueEvent(listener)
    }

    override fun signIn(username: String, password: String): Single<String> {
        return Single.create { emitter -> firebaseSignIn(emitter, username, password) }
    }
}