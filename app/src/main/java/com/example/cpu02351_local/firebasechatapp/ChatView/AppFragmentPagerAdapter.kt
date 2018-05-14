package com.example.cpu02351_local.firebasechatapp.ChatView

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatView.ContactList.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.ChatView.ConversationList.ConversationListFragment

class AppFragmentPagerAdapter(fm: FragmentManager, mViewModel: ChatViewModel) : FragmentPagerAdapter(fm) {

    private val fragments = arrayOf(
            ConversationListFragment.newInstance(mViewModel),
            ContactListFragment.newInstance(mViewModel))
    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size


}
