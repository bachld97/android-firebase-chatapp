package com.example.cpu02351_local.firebasechatapp.loginscreen

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class AuthenticationViewModel(
        private val authenticator: ChatAuthenticator,
        private val callback: AuthenticationCallback?) {

    var username =""
    var password =""

    private val authenticateResultHandle = object : SingleObserver<String> {
        private lateinit var disposable: Disposable
        override fun onSuccess(t: String) {
            callback?.onAuthenticationSuccess(t)
            if (!disposable.isDisposed) disposable.dispose()
        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onError(e: Throwable) {
            callback?.onAuthenticationError(e.message ?: "Sorry, something wrong happened")
            if (!disposable.isDisposed) disposable.dispose()
        }
    }

    fun signIn() {
        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signIn(username, encryptedPass).subscribe(authenticateResultHandle)
    }

    fun createAccount() {
        if (username.isEmpty() || password.isEmpty()) {
            callback?.onAuthenticationError("Username and password cannot be empty")
            return
        }

        val encryptedPass = PasswordEncryptor.encrypt(password)
        authenticator.signUp(username, encryptedPass).subscribe(authenticateResultHandle)
    }

    fun dispose() {
        authenticator.dispose()
    }
}