package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail.UserDetailFragment

class AppFragmentPagerAdapter(fm: FragmentManager, private val userId: String) : FragmentPagerAdapter(fm) {

    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ConversationListFragment.newInstance(userId)
            1-> ContactListFragment.newInstance(userId)
            else -> UserDetailFragment.newInstance(userId)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = 3

    /*
    override fun getItemId(position: Int): Long {
        // Position can only be 0, 1, 2 so only need to bit to represents
        return (instanceCount shl 2) or position.toLong()
    } */
}
