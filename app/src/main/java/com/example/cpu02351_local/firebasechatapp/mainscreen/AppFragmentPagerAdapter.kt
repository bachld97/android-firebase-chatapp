package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactItem
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.ConversationListFragment
import com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail.UserDetailFragment
import com.example.cpu02351_local.firebasechatapp.utils.ContactConsumerView

class AppFragmentPagerAdapter(fm: FragmentManager, userId: String) : FragmentPagerAdapter(fm) {
    private val fragments =  arrayOf(
                ConversationListFragment.newInstance(userId),
                ContactListFragment.newInstance(userId),
                UserDetailFragment.newInstance(userId))

    companion object {
        @JvmStatic
        var instanceCount = 0L
    }

    init {
        instanceCount++
    }

    private val titles = arrayOf("Chat", "Contact", "Setting")

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    override fun getCount(): Int = fragments.size

    fun dispatchNetworkResult(res: List<ContactItem>?) {
        if (res == null) return
        fragments.mapNotNull { it as? ContactConsumerView }
                .forEach { it.onNetworkContactsLoaded(res) }
    }

    fun dispatchLocalResult(res: List<ContactItem>?) {
        if (res == null) return
        fragments.mapNotNull { it as? ContactConsumerView }
                .forEach { it.onLocalContactsLoaded(res) }
    }

    /*
    override fun getItemId(position: Int): Long {
        // Position can only be 0, 1, 2 so only need to bit to represents
        return (instanceCount shl 2) or position.toLong()
    } */
}
