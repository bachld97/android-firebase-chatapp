package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

import io.reactivex.Single

class AuthenticateViewModel(
        private val authenticator: ChatAuthenticator,
        private val callback: AuthenticationCallback) {

    var username =""
    var password =""

    fun signIn() {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        callback.onCallbackResult(authenticator.signIn(username, encryptedPass))
    }

    fun createAccount() {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onCallbackResult(Single.error(Throwable("Username and password cannot be blank")))
            return
        }

        val encryptedPass = PasswordEncryptor.encrypt(password)
        callback.onCallbackResult(authenticator.signUp(username, encryptedPass))
    }
}