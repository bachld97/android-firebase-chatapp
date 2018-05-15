package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

interface AuthenticationObserver {
    fun onAuthenticationResult(result: Boolean, userId: String)
}