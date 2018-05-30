package com.example.cpu02351_local.firebasechatapp.utils

import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactItem

interface ContactConsumerView {
    fun onNetworkContactsLoaded(res: List<ContactItem>)
    fun onLocalContactsLoaded(res: List<ContactItem>)
}