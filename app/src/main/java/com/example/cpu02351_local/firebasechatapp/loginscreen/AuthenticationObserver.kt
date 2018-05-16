package com.example.cpu02351_local.firebasechatapp.loginscreen

import io.reactivex.Single

interface AuthenticationObserver {
    fun onAuthenticationResult(result: Single<String>)
}