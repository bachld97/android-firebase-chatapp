package com.example.cpu02351_local.firebasechatapp.ChatUi.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.ChatUi.Fragments.ConversationListFragment

class AppFragmentPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    val fragments = arrayOf<Fragment>(
            ConversationListFragment.newInstance(),
            ConversationListFragment.newInstance())
    val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size


}
