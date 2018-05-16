package com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication

import io.reactivex.Single

interface AuthenticationCallback {
    fun onCallbackResult(result: Single<String>)
}