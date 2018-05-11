package com.example.cpu02351_local.firebasechatapp.ChatUi.ContactList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListContactDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.NetworkDataSource
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

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

    override fun onDataLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}