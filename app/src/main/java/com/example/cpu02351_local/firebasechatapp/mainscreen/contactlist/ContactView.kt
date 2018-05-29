package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

interface ContactView {
    fun onNetworkContactsLoaded(res: List<ContactItem>)
    fun onLocalContactsLoaded(res: List<ContactItem>)
    fun navigate(conversationId: String, userIds: String)
    fun onContactItemSelected(selectedPosition: List<Int>)
}