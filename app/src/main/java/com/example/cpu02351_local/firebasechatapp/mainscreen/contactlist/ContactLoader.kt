package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import io.reactivex.Observable
import io.reactivex.Single

interface ContactLoader {
    fun loadContacts(userId: String): Observable<List<User>>
    fun loadContact(contactId: String): Single<User>
}