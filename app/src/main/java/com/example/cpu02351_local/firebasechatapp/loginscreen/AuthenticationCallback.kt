package com.example.cpu02351_local.firebasechatapp.loginscreen

interface AuthenticationCallback {
    fun onAuthenticationSuccess(approvedUser: String)
    fun onAuthenticationError(errorMessage: String)
}