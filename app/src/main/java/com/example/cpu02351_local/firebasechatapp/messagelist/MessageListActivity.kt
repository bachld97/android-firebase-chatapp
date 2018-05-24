package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ActivityMessageListBinding
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalUserDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalUserDatabase
import com.example.cpu02351_local.firebasechatapp.loginscreen.LogInHelper
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.Message
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_message_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MessageListActivity :
        MessageView,
        AppCompatActivity() {

    @Inject
    lateinit var localUserDatabase: RoomLocalUserDatabase

    private lateinit var mConversationId: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MessageListAdapter
    private lateinit var mByUsers: String
    private lateinit var mLoggedInUser: String
    private val mMessageLoader: MessageLoader = FirebaseMessageLoader()
    private lateinit var mMessageViewModel: MessageViewModel


    companion object {
        const val BY_USERS_STRING = "by_users_string"
        const val CONVERSATION_ID = "conversation_id"

        @JvmStatic
        fun newInstance(context: Context, conId: String, byUsers: String? = null): Intent {
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
        mConversationId = intent.getStringExtra(CONVERSATION_ID)
        mByUsers = intent.getStringExtra(BY_USERS_STRING)

        val binding = DataBindingUtil.setContentView<ActivityMessageListBinding>(this, R.layout.activity_message_list)
        mMessageViewModel = MessageViewModel(mMessageLoader, this, mConversationId)
        binding.viewModel = mMessageViewModel
        binding.executePendingBindings()

        init()
    }

    private fun init() {
        mLoggedInUser = LogInHelper.getLoggedInUser(applicationContext)
        mRecyclerView = findViewById(R.id.conversationContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        val endlessLoader = object : EndlessLoader {
            override fun loadMore() {
                mMessageViewModel.loadMessages()
            }
        }

        mAdapter = MessageListAdapter(ArrayList(), mLoggedInUser, mRecyclerView, endlessLoader)
        mRecyclerView.adapter = mAdapter

        DaggerRoomLocalUserDatabaseComponent
                .builder()
                .contextModule(ContextModule(this))
                .build()
                .injectInto(this)

        loadAvas()

    }

    private fun loadAvas() {
        localUserDatabase.loadByIds(mByUsers.split(Conversation.ID_DELIM))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { res ->
                    val avaMap = HashMap<String, String>()
                    val nameMap = HashMap<String, String>()
                    res.forEach {
                        avaMap[it.id] = it.avaUrl
                        nameMap[it.id] = it.name
                    }
                    mAdapter.updateInfoMaps(avaMap, nameMap)
                }
    }

    override fun onError() {
        // Do nothing
    }

    override fun addMessage() {
        val m = Message(UUID.randomUUID().toString())
        m.byUser = getLoggedInUser()
        m.content = mess.text.toString()
        m.atTime = System.currentTimeMillis()
        mMessageViewModel.sendMessage(mConversationId, m, intent.getStringExtra(BY_USERS_STRING))
        mess.text.clear()
    }

    private fun getLoggedInUser(): String {
        return mLoggedInUser
    }

    override fun onStop() {
        super.onStop()
        mMessageViewModel.dispose()
    }
}
