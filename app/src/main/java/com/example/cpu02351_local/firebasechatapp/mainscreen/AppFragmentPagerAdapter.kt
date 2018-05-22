package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail.UserDetailFragment

class AppFragmentPagerAdapter(fm: FragmentManager, userId: String) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            ConversationListFragment.newInstance(userId),
            ContactListFragment.newInstance(userId),
            UserDetailFragment.newInstance(userId))
    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size

}
