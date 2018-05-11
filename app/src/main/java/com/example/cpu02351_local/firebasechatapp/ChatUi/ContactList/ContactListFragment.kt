package com.example.cpu02351_local.firebasechatapp.ChatUi.ContactList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListContactDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class ContactListFragment :
        ListContactDisplayUnit,
        Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() : ContactListFragment {
            return ContactListFragment()
        }
    }

    private val networkDataSource: NetworkDataSource = FirebaseNetworkDataSource()
    private val chatController = ChatController(networkDataSource)
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ContactListAdapter


    override fun displayThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun onDataLoaded(result: java.util.ArrayList<User>) {
        mAdapter.updateContacts(result.sortedWith(kotlin.Comparator { o1, o2 ->  o1.name.compareTo(o2.name, true)}))
    }

    override fun onDataError(message: String?) {

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
        chatController.loadContact(this, currentUser())
    }

    private fun currentUser(): String {
        return "user1"
    }

    override fun onStop() {
        super.onStop()
        chatController.dispose()
    }
}