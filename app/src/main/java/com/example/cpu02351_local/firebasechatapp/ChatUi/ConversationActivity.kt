package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListMessageDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class ConversationActivity :
        ListMessageDisplayUnit,
        AppCompatActivity() {

    override fun onStop() {
        super.onStop()
        chatController.dispose()
    }

    override fun getDisplayThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun onSuccessfulLoadMessage(result: ArrayList<Message>) {
        mAdapter.updateList(result)
    }

    override fun onFailLoadMessage(errorMessage: String?) {
        // Should introduce an error code here :)
        // Do nothing for now :D
    }

    private lateinit var mConversationId : String
    private val networkDataSource = FirebaseNetworkDataSource()
    private val chatController = ChatController(networkDataSource)
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)
        mRecyclerView = findViewById(R.id.conversationListContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        mAdapter = ConversationAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.adapter = mAdapter
        init()
    }

    override fun onStart() {
        super.onStart()
        loadMessages()
    }

    private fun loadMessages() {
        chatController.loadMessages(this, mConversationId)
    }

    private fun init() {
        mConversationId = intent.getStringExtra("conversationId")
        checkNotNull(mConversationId)
        Toast.makeText(this, "Retrieved Id: $mConversationId", Toast.LENGTH_SHORT).show()
    }

}
