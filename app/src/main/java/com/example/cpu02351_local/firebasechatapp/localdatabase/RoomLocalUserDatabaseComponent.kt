package com.example.cpu02351_local.firebasechatapp.localdatabase

import com.example.cpu02351_local.firebasechatapp.mainscreen.MainActivity
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactLoader
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListAdapter
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListFragment
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class])
interface RoomLocalUserDatabaseComponent {
    fun injectInto(dest: MessageListActivity)
    fun injectInto(dest: ConversationListFragment)
    fun injectInto(dest: ContactListFragment)
    fun injectInto(dest: MainActivity)
}