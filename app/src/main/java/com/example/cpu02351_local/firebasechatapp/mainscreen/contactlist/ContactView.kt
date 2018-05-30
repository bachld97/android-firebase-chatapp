package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.utils.ContactConsumerView

interface ContactView : ContactConsumerView {
    fun navigate(conversationId: String, userIds: String)
    fun onContactItemSelected(selectedPosition: List<Int>)
}