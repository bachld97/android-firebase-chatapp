package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

import io.reactivex.Single

class AuthenticateViewModel(
        private val authenticator: ChatAuthenticator,
        private val callback: AuthenticationCallback) : AuthenticationObserver {

    var username =""
    var password =""

    override fun onAuthenticationResult(result: Single<String>) {
        callback.onCallbackResult(result)
    }

    fun signIn() {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signIn(username, encryptedPass, this)
    }

    fun createAccount() {
        if (username.isEmpty() || password.isEmpty()) {
            callback.onCallbackResult(Single.error(Throwable("Username and password cannot be blank")))
            return
        }

        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signUp(username, encryptedPass, this)
    }
}