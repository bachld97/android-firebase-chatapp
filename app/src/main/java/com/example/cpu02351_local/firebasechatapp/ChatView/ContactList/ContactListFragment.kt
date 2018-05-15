package com.example.cpu02351_local.firebasechatapp.ChatView.ContactList

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatView.MessageList.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.ContactViewObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.R
import java.util.*

class ContactListFragment :
        ContactViewObserver,
        Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(mViewModel: ChatViewModel): ContactListFragment {
            val res = ContactListFragment()
            res.mViewModel = mViewModel
            return res
        }
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactListAdapter
    private lateinit var mViewModel: ChatViewModel
    private lateinit var mCreateGroupChat: FloatingActionButton

    override fun onContactsLoaded(contacts: List<User>) {
        mAdapter.updateContacts(contacts.sortedWith(kotlin.Comparator { c1, c2 ->  c1.name.compareTo(c2.name, true)}))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_contact_list, container, false)
        mRecyclerView = v.findViewById(R.id.recyclerView)
        mAdapter = ContactListAdapter(ArrayList(), mRecyclerView, mViewModel)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
        mCreateGroupChat = v.findViewById(R.id.createGroupChat)

        mCreateGroupChat.setOnClickListener {
            val users = arrayOf(
                        User(currentUser()),
                        User("user2"),
                        User("user3"))
            val intent = Intent(context, MessageListActivity::class.java)
            intent.putExtra("conversationId", Conversation.uniqueId(users))
            intent.putExtra("byUsers", users.joinToString(Conversation.ID_DELIM))
            context?.startActivity(intent)
        }
        return v
    }

    override fun onStart() {
        super.onStart()
        mViewModel.register(this)
    }

    private fun currentUser(): String {
        return "user1"
    }

    override fun onStop() {
        super.onStop()
        mViewModel.unregister(this)
    }
}