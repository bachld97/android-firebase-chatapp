package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import java.util.*
import javax.inject.Inject

class ContactListFragment :
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

    private lateinit var mAdapter2: ContactItemAdapter
    private lateinit var mAdapter: ContactListAdapter
    private var mContactLoader: ContactLoader = FirebaseContactLoader()
    private lateinit var mContactViewModel: ContactViewModel
    private lateinit var mCreateGroupChat: FloatingActionButton

    @Inject
    lateinit var localDatabase: RoomLocalDatabase

    private fun init() {
        mContactViewModel = ContactViewModel(mContactLoader, this, userId)
        mAdapter2 = ContactItemAdapter()
        // mAdapter = ContactListAdapter(ArrayList(), mRecyclerView, mContactViewModel)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        // mRecyclerView.adapter = mAdapter
        mRecyclerView.adapter = mAdapter2

        DaggerRoomLocalDatabaseComponent
                .builder()
                .contextModule(ContextModule(this.context!!))
                .build()
                .injectInto(this)

        mContactViewModel.setLocalUserDatabase(localDatabase)
    }

    private fun dispose() {
        mContactViewModel.dispose()
    }

    override fun onContactsLoaded(res: List<User>) {
        // mAdapter.updateContacts(res.sortedWith(kotlin.Comparator { c1, c2 ->  c1.name.compareTo(c2.name, true)}))
       mAdapter2.setItems(res.map { ContactItem(it) }, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_contact_list, container, false)
        mRecyclerView = v.findViewById(R.id.recyclerView)

        mCreateGroupChat = v.findViewById(R.id.createGroupChat)
        mCreateGroupChat.visibility = View.INVISIBLE
        /*
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
        */
        init()
        return v
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