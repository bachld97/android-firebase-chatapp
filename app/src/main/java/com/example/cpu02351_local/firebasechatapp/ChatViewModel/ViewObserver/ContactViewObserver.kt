package com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User

interface ContactViewObserver {
    fun onContactsLoaded(contacts: List<User>)
}