package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatAuthenticator
import com.google.firebase.database.FirebaseDatabase

class FirebaseChatAuthenticator : ChatAuthenticator() {
    private val database = FirebaseHelper.getFirebaseInstance()
    private val databaseRef = database.reference!!

    override fun signUpSync(username: String, password: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValidSync(username: String, password: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}