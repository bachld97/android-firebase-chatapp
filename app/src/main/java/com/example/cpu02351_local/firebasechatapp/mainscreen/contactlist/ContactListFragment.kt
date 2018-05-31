package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.FragmentContactListBinding
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.utils.ContactConsumerView
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import javax.inject.Inject

class ContactListFragment :
        ContactConsumerView,
        ContactView,
        Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(userId: String): ContactListFragment {
            val temp = ContactListFragment()
            val args = Bundle()
            args.putString("userId", userId)
            temp.arguments = args
            return temp
        }
    }

    private lateinit var userId: String
    private lateinit var mRecyclerView: RecyclerView

    private var mAdapter: ContactItemAdapter? = null
    private lateinit var mContactViewModel: ContactViewModel
    private lateinit var mCreateGroupChat: FloatingActionButton
    private var temContactItem: List<ContactItem>? = null


    private fun init() {
        mContactViewModel = ContactViewModel(this, userId)
    }

    private fun dispose() {
    }

    override fun navigate(conversationId: String, userIds: String) {
        val _context = context
        if (_context != null) {
            val intent = MessageListActivity.newInstance(context!!, conversationId, userIds)
            _context.startActivity(intent)
        }
        mAdapter?.clearSelected()
    }

    override fun onContactItemSelected(selectedPosition: List<Int>) {
        if (selectedPosition.isNotEmpty()) {
            mCreateGroupChat.show()
        } else {
            mCreateGroupChat.hide()
        }
    }

    override fun onLocalContactsLoaded(res: List<ContactItem>) {
        Log.d("DEBUG_LC", "localResult ${res.size}, mAdapter null state: ${mAdapter == null}, fragment: ${this}")
        mAdapter?.setItems(res, false)
    }

    override fun onNetworkContactsLoaded(res: List<ContactItem>) {
        Log.d("DEBUG_LC", "networkResult ${res.size}, mAdapter null state: ${mAdapter == null}, fragment: ${this}")
       mAdapter?.setItems(res, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        Log.d("DEBUG_LC", "onCreate, fragment: ${this}")
    }

    private lateinit var mBinding: FragmentContactListBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_list, container, false)
        val v = mBinding.root
        mRecyclerView = v.findViewById(R.id.recyclerView)
        mCreateGroupChat = v.findViewById(R.id.createGroupChat)
        mCreateGroupChat.visibility = View.INVISIBLE
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mBinding.viewModel = mContactViewModel
        mBinding.executePendingBindings()
        Log.d("DEBUG_LC", "createView, fragment: ${this}")
        return v
    }


    override fun onStart() {
        super.onStart()
        mAdapter = ContactItemAdapter(mContactViewModel, mRecyclerView)
        mRecyclerView.adapter = mAdapter
        Log.d("DEBUG_LC", "onStart - mAdapter created here, fragment: ${this}")
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        userId = args?.get("userId") as String
    }

    override fun onStop() {
        super.onStop()
        dispose()
    }
}