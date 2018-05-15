package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

abstract class ChatAuthenticator {
    abstract fun signUp(username: String, password: String, authenticationObserver: AuthenticationObserver)
    abstract fun signIn(username: String, password: String, authenticationObserver: AuthenticationObserver)
}