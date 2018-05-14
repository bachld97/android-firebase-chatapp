package com.example.cpu02351_local.firebasechatapp.ChatView.MessageList

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.MessageViewObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource
import com.example.cpu02351_local.firebasechatapp.R
import kotlinx.android.synthetic.main.activity_message_list.*
import java.util.*

class MessageListActivity :
        MessageViewObserver,
        AppCompatActivity() {

    private lateinit var mConversationId : String
    private val mChatModel = FirebaseChatDataSource()
    private lateinit var mChatViewModel: ChatViewModel
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MessageListAdapter

    override fun onMessagesLoaded(messages: List<Message>) {
        Log.d("DEBUGGING", "Messages: ${messages.size}")
        val m = messages.sortedWith(kotlin.Comparator { m1, m2 ->
            m1.atTime.compareTo(m2.atTime)
        })
        mAdapter.updateList(m)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        mChatViewModel = ChatViewModel(mChatModel, getLoggedInUser())
        mRecyclerView = findViewById(R.id.conversationContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        mAdapter = MessageListAdapter(ArrayList(), mRecyclerView)
        mRecyclerView.adapter = mAdapter
        mConversationId = intent.getStringExtra("conversationId")

        registerOnClick()
    }

    private fun registerOnClick() {
        sendMes.setOnClickListener {
            if (mess.text.isNotEmpty()) {
                val m = Message(UUID.randomUUID().toString())
                m.byUser = getLoggedInUser()
                m.content = mess.text.toString()
                m.atTime = System.currentTimeMillis()
                mChatViewModel.sendMessage(mConversationId, m, intent.getStringExtra("byUsers"))
                mess.text.clear()
            }
        }
    }

    private fun getLoggedInUser(): String {
        return "user1"
    }

    override fun onStart() {
        super.onStart()
        mChatViewModel.init()
        mChatViewModel.register(this)
        mChatViewModel.loadMessages(mConversationId)
    }

    override fun onStop() {
        super.onStop()
        mChatViewModel.unregister(this)
        mChatViewModel.dispose()
    }
}
