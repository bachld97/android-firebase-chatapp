package com.example.cpu02351_local.firebasechatapp.utils

import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactItem

data class ContactItemEvent(var contactItems: List<ContactItem>, var isFromNetwork: Boolean)