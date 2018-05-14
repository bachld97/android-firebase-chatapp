package com.example.cpu02351_local.firebasechatapp.ChatView.ConversationList

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.ConversationViewObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.R

class ConversationListFragment :
        ConversationViewObserver,
        Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(mViewModel: ChatViewModel): ConversationListFragment {
            val tem = ConversationListFragment()
            tem.mViewModel = mViewModel
            return tem
        }
    }
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConversationListAdapter
    private lateinit var mViewModel: ChatViewModel

    override fun onConversationsLoaded(conversations: List<Conversation>) {
        val c = conversations.sortedWith(Comparator { c1, c2 ->
            c2.createdTime.compareTo(c1.createdTime)
        })
        mAdapter.updateList(c)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        mRecyclerView = root.findViewById(R.id.conversationListContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mAdapter = ConversationListAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.adapter = mAdapter
        return root
    }

    override fun onStart() {
        super.onStart()
        mViewModel.register(this)
    }

    override fun onStop() {
        super.onStop()
        mViewModel.unregister(this)
    }
}