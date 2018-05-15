package com.example.cpu02351_local.firebasechatapp.ChatViewModel

abstract class ChatAuthenticator {
    abstract fun signUpSync(username: String, password: String) : Boolean
    abstract fun isValidSync(username: String, password: String) : Boolean

    // This method will be run synchronously
    fun signUpFromClient(username: String, password: String): Boolean {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        return signUpSync(username, encryptedPass)
    }

    // This method will be run synchronously
    fun isSignInValidFromClient(username: String, password: String): Boolean {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        return isValidSync(username, encryptedPass)
    }
}