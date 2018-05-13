package com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User

interface ContactDataObserver {
    fun onContactsLoaded(contacts: List<User>)
}