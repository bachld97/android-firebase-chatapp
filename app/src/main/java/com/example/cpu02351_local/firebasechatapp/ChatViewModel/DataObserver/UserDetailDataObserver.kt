package com.example.cpu02351_local.firebasechatapp.ChatViewModel.DataObserver

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User

interface UserDetailDataObserver {
    fun onUserDetailLoaded(user: User)
}