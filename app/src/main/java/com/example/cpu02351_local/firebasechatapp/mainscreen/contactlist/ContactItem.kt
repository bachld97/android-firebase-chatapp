package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ContactItem(user: User) : ListItem {
    override fun equalsItem(otherItem: ListItem): Boolean {
        return otherItem is ContactItem && this.contactId == otherItem.contactId
    }

    override fun equalsContent(otherItem: ListItem): Boolean = equalsItem(otherItem)

    var isSelected = false
    val contactName = user.name
    val avaUrl = user.avaUrl
    val contactId = user.id
}