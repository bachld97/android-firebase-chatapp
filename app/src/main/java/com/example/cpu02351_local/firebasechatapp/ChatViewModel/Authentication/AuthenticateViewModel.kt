package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

class AuthenticateViewModel(
        private val authenticator: ChatAuthenticator,
        private val callback: AuthenticationCallback) : AuthenticationObserver {
    override fun onAuthenticationResult(result: Boolean, userId: String) {
        callback.onCallbackResult(result, userId)
    }

    fun signIn(username: String, rawPass: String) {
        val encryptedPass = PasswordEncryptor.encrypt(rawPass)
        authenticator.signIn(username, encryptedPass, this)
    }

    fun createAccount(username: String, rawPass: String) {
        val encryptedPass = PasswordEncryptor.encrypt(rawPass)
        authenticator.signUp(username, encryptedPass, this)
    }
}