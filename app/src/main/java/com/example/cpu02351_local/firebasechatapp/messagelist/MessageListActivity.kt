package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.cpu02351_local.firebasechatapp.model.Message
import com.example.cpu02351_local.firebasechatapp.loginscreen.LogInHelper
import com.example.cpu02351_local.firebasechatapp.R
import kotlinx.android.synthetic.main.activity_message_list.*
import java.util.*

class MessageListActivity :
        MessageView,
        AppCompatActivity() {

    private lateinit var mConversationId : String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MessageListAdapter
    private lateinit var mLoggedInUser: String
    private val mMessageLoader: MessageLoader = FirebaseMessageLoader()
    private lateinit var mMessageViewModel: MessageViewModel


    companion object {
        const val BY_USERS_STRING = "by_users_string"
        const val CONVERSATION_ID = "conversation_id"

        @JvmStatic
        fun newInstance(context: Context, conId: String, byUsers: String? = null) : Intent {
            val intent = Intent(context, MessageListActivity::class.java)
            intent.putExtra(CONVERSATION_ID, conId)
            intent.putExtra(BY_USERS_STRING, byUsers ?: "")
            return intent
        }
    }

    override fun onNewMessage(message: Message) {
        mAdapter.addMessage(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        mLoggedInUser = LogInHelper.getLoggedInUser(applicationContext)
        mRecyclerView = findViewById(R.id.conversationContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        mAdapter = MessageListAdapter(ArrayList(), mLoggedInUser)
        mRecyclerView.adapter = mAdapter
        mConversationId = intent.getStringExtra(CONVERSATION_ID)
        mMessageViewModel = MessageViewModel(mMessageLoader, this, mConversationId)
        registerOnClick()
    }

    private fun registerOnClick() {
        sendMes.setOnClickListener {
            if (mess.text.isNotEmpty()) {
                val m = Message(UUID.randomUUID().toString())
                m.byUser = getLoggedInUser()
                m.content = mess.text.toString()
                m.atTime = System.currentTimeMillis()
                mMessageViewModel.sendMessage(mConversationId, m, intent.getStringExtra(BY_USERS_STRING))
                mess.text.clear()
            }
        }
    }

    override fun onError() {
        // Do nothing
    }

    private fun getLoggedInUser(): String {
        return mLoggedInUser
    }

    override fun onStop() {
        super.onStop()
        mMessageViewModel.dispose()
    }
}
