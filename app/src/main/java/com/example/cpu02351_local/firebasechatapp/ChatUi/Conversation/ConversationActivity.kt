package com.example.cpu02351_local.firebasechatapp.ChatUi.Conversation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.ChatCore.ChatController
import com.example.cpu02351_local.firebasechatapp.ChatCore.boundary.ListMessageDisplayUnit
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseNetworkDataSource
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_conversation.*
import java.util.*

class ConversationActivity :
        ListMessageDisplayUnit,
        AppCompatActivity() {

    override fun onStop() {
        super.onStop()
        chatController.dispose()
    }

    override fun displayThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun onDataLoaded(result: ArrayList<Message>) {
        result.sortWith(Comparator { m1, m2 -> m1.atTime.compareTo(m2.atTime) })
        mAdapter.updateList(result)
    }

    override fun onDataError(errorMessage: String?) {
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
        setContentView(R.layout.activity_conversation)
        mRecyclerView = findViewById(R.id.conversationContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        mAdapter = ConversationAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.adapter = mAdapter
        init()
    }

    override fun onStart() {
        super.onStart()
        loadMessages()
        sendMes.setOnClickListener {
            if (mess.text.toString().isNotEmpty()) {
                val timestamp = Date().time
                chatController.addMessage(this, mConversationId, "user1", "text", mess.text.toString(), timestamp)
            }
            mess.text.clear()
            mRecyclerView.smoothScrollToPosition(0)
        }
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
