package com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User

interface ContactViewObserver {
    fun onContactsLoaded(contacts: List<User>)
}