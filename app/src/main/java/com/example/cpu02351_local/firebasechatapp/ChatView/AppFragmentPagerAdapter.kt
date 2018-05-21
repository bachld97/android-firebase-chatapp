package com.example.cpu02351_local.firebasechatapp.ChatView

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.ChatView.ContactList.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListFragment
import com.example.cpu02351_local.firebasechatapp.ChatView.UserDetail.UserDetailFragment
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel

class AppFragmentPagerAdapter(fm: FragmentManager, userId: String) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            ConversationListFragment.newInstance(userId))
            // ContactListFragment.newInstance(mViewModel),
            // UserDetailFragment.newInstance(mViewModel))
    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size


}
