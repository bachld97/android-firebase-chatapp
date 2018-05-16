package com.example.cpu02351_local.firebasechatapp.loginscreen

import io.reactivex.Single

abstract class ChatAuthenticator {
    abstract fun signUp(username: String, password: String): Single<String>
    abstract fun signIn(username: String, password: String): Single<String>
    abstract fun dispose()
}