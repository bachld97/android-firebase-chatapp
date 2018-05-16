package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

interface AuthenticationCallback {
    fun onAuthenticationSuccess(approvedUser: String)
    fun onAuthenticationError(errorMessage: String)
}