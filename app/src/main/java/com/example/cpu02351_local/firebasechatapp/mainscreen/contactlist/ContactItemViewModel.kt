package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

class ContactItemViewModel(var contactItem: ContactItem) {
    fun getContactName(): String = contactItem.contactName
    fun getContactAvaUrl(): String = contactItem.avaUrl
    fun getContactId(): String = contactItem.contactId
}