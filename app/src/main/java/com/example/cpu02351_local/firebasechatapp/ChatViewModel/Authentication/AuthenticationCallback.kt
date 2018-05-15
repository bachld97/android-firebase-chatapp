package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

interface AuthenticationCallback {
    fun onCallbackResult(isSuccessful: Boolean, userId: String)
}