package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ContactItem(user: User) : ListItem {
    val contactName = user.name
    val avaUrl = user.avaUrl
    val contactId = user.id
}