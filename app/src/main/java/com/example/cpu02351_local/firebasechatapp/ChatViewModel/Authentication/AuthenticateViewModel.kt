package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

class AuthenticateViewModel(
        private val authenticator: ChatAuthenticator,
        private val callback: AuthenticationCallback) : AuthenticationObserver {

    var username =""
    var password =""

    override fun onAuthenticationResult(result: Boolean, userId: String) {
        callback.onCallbackResult(result, userId)
    }

    fun signIn() {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signIn(username, encryptedPass, this)
    }

    fun createAccount() {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signUp(username, encryptedPass, this)
    }
}