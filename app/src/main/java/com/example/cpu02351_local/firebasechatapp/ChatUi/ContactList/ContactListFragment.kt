package com.example.cpu02351_local.firebasechatapp.ChatUi.ContactList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatCore.ViewObserver.ContactViewObserver
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListContactDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
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

    override fun onContactsLoaded(contacts: List<User>) {
        mAdapter.updateContacts(contacts.sortedWith(kotlin.Comparator { c1, c2 ->  c1.name.compareTo(c2.name, true)}))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_contact_list, container, false)
        mRecyclerView = v.findViewById(R.id.recyclerView)
        mAdapter = ContactListAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
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