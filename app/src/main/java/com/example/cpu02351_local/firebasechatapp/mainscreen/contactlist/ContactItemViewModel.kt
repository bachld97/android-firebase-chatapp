package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

class ContactItemViewModel(var contactItem: ContactItem,
                           private var callback: ContactItemAdapter.ItemSelectedCallback) {

    fun getContactName(): String = contactItem.contactName
    fun getContactAvaUrl(): String = contactItem.avaUrl
    fun getContactId(): String = contactItem.contactId

    fun isSelected() = contactItem.isSelected

    fun avaSelected() {
        callback.onItemSelected(contactItem)
    }
}