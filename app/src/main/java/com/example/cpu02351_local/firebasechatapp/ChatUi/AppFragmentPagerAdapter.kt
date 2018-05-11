package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.ChatUi.ContactList.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.ChatUi.ConversationList.ConversationListFragment

class AppFragmentPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf<Fragment>(
            ConversationListFragment.newInstance(),
            ContactListFragment.newInstance())
    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size


}
