package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User

interface ContactView {
    fun onContactsLoaded(res: List<User>)
}