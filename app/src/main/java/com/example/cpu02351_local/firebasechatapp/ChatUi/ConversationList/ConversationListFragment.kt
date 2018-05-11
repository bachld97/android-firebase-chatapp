package com.example.cpu02351_local.firebasechatapp.ChatUi.ConversationList

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlin.collections.ArrayList

class ConversationListFragment :
        ListConversationDisplayUnit,
        Fragment() {


    companion object {
        @JvmStatic
        fun newInstance() : ConversationListFragment {
            return ConversationListFragment()
        }
    }

    private val networkDataSource = FirebaseNetworkDataSource()
    private val chatController = ChatController(networkDataSource)
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConversationListAdapter
    private lateinit var mFloatingActionButton : FloatingActionButton

    override fun displayThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun onDataLoaded(result: ArrayList<Conversation>) {
        result.sortWith(Comparator {c1, c2 ->
            c2.createdTime!!.compareTo(c1.createdTime!!)})
        mAdapter.updateList(result)
    }

    override fun onDataError(errorMessage: String?) {
        // Do nothing for now
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        mRecyclerView = root.findViewById(R.id.conversationListContainer)
        mFloatingActionButton = root.findViewById(R.id.fabAddConversation)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ConversationListAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.adapter = mAdapter
        return root
    }

    override fun onStart() {
        super.onStart()
        chatController.loadConversations(this, currentUser())

        // Test button, will be removed when app updates
        mFloatingActionButton.setOnClickListener {
            chatController.addConversation(this, arrayListOf(currentUser(), "user2"))
        }
    }

    private fun currentUser(): String {
        return "user1"
    }

    override fun onStop() {
        super.onStop()
        chatController.dispose()
    }
}