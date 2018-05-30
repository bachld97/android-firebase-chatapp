package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.utils.ContactProducerViewModel
import java.util.*

class ContactViewModel(contactLoader: ContactLoader,
                       private val contactView: ContactView,
                       private val userId: String) : ContactProducerViewModel(contactLoader, contactView, userId) {

    fun onSelectedContactsAction(contacts: List<ContactItem>) {
        val userIdString = contacts.joinToString(Conversation.ID_DELIM) { it.contactId }
                .plus("${Conversation.ID_DELIM}$userId")
        val userIds = userIdString.split(Conversation.ID_DELIM).toTypedArray()
        val conId = Conversation.uniqueId(userIds)
        contactView.navigate(conId, userIdString)
    }

    private var selectedMessageItem = Collections.emptyList<ContactItem>()
    fun createGroupChat() {
        onSelectedContactsAction(selectedMessageItem)
    }

    fun onSelectedContactChanged(selectedPosition: List<Int>, selectedMessageItem: List<ContactItem>) {
        this.selectedMessageItem = selectedMessageItem
        contactView.onContactItemSelected(selectedPosition)
    }
}