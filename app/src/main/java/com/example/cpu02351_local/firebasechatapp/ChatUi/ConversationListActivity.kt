package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListConversationDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class ConversationListActivity :
        ListConversationDisplayUnit,
        AppCompatActivity() {

    private val networkDataSource = FirebaseNetworkDataSource()
    private val chatController = ChatController(networkDataSource)
    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: ConversationListAdapter = ConversationListAdapter(ArrayList())
    private lateinit var mFloatingActionButton : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)
        mRecyclerView = findViewById(R.id.conversationListContainer)
        mFloatingActionButton = findViewById(R.id.fabAddConversation)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        loadConversations()
        mFloatingActionButton.setOnClickListener {
            chatController.addConversation(this, arrayListOf("user1", "user2"))
        }
    }


    private fun loadConversations() {
        chatController.loadConversations(this, "user1")
    }

    override fun getDisplayThread() : Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun onSuccessfulLoadConversations(result: ArrayList<Conversation>) {
       mAdapter.updateList(result)
    }

    override fun onFailLoadConversation(errorMessage: String?) {
        // display error
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
