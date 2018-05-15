package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.PASSWORD
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.AuthenticationObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.ChatAuthenticator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FirebaseChatAuthenticator : ChatAuthenticator() {
    private val database = FirebaseHelper.getFirebaseInstance()
    private val databaseRef = database.reference!!

    override fun signUp(username: String, password: String, authenticationObserver: AuthenticationObserver) {
        Log.d("DEBUGGING", "Sign up clicked")
        databaseRef.child(USERS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                Log.d("DEBUGGING", "sign up callback")

                if (snapshot!!.hasChild(username)) {
                    authenticationObserver.onAuthenticationResult(false, username)
                    return
                }
                val u = FirebaseUser(username, password)
                snapshot.ref.child(username).setValue(u.toMap())
                authenticationObserver.onAuthenticationResult(true, username)
            }
            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing for now
            }
        })
    }

    override fun signIn(username: String, password: String, authenticationObserver: AuthenticationObserver) {
        databaseRef.child("$USERS/$username").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot?) {
                if (snapshot?.value != null) {
                    if (snapshot.child(PASSWORD).value == password) {
                        authenticationObserver.onAuthenticationResult(true, username)
                        return
                    }
                }
                authenticationObserver.onAuthenticationResult(false, username)
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Do nothing :)
            }
        })
    }

}