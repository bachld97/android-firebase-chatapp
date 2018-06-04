package com.example.cpu02351_local.firebasechatapp.messagelist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.ImageView
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ActivityMessageListBinding
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.loginscreen.LogInHelper
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import junit.framework.Assert
import kotlinx.android.synthetic.main.item_conversation_group3_list.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MessageListActivity :
        MessageView,
        AppCompatActivity() {

    @Inject
    lateinit var localDatabase: RoomLocalDatabase

    private lateinit var mConversationId: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter2: MessageItemAdapter
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

    override fun onRequestSendMessage() {
        // mAdapter.addMessage(message)
        mBinding.invalidateAll()
    }


    private lateinit var mBinding: ActivityMessageListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mConversationId = intent.getStringExtra(CONVERSATION_ID)
        mByUsers = intent.getStringExtra(BY_USERS_STRING)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_list)
        mMessageViewModel = MessageViewModel(mMessageLoader, this, mConversationId)
        mBinding.viewModel = mMessageViewModel
        mBinding.executePendingBindings()
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        toolbar.alpha = 0.2f
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        mLoggedInUser = LogInHelper.getLoggedInUser(applicationContext)
        mRecyclerView = findViewById(R.id.conversationContainer)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        val endlessLoader = object : EndlessLoader {
            override fun loadMore() {
                mMessageViewModel.loadMore()
            }
        }

        // mAdapter = MessageListAdapter(ArrayList(), mLoggedInUser, mRecyclerView, endlessLoader)
        mAdapter2 = MessageItemAdapter(this)
        mRecyclerView.adapter = mAdapter2

        DaggerRoomLocalDatabaseComponent
                .builder()
                .contextModule(ContextModule(this))
                .build()
                .injectInto(this)
        mMessageViewModel.setLocalDatabase(localDatabase)
        loadUsers()
    }

    override fun updateMessageItem(messages: List<MessageItem>) {
        mAdapter2.setItems(messages, true)
    }

    private fun loadUsers() {
        localDatabase.loadUserByIds(mByUsers.split(Conversation.ID_DELIM))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { res ->
                    val userMap = HashMap<String, User>()
                    val avaMap = HashMap<String, String>()
                    val nameMap = HashMap<String, String>()
                    res.forEach {
                        avaMap[it.id] = it.avaUrl
                        nameMap[it.id] = it.name
                        userMap[it.id] = it
                    }
                    // mAdapter.updateInfoMaps(avaMap, nameMap)
                    mAdapter2.updateUserInfo(userMap)
                }
    }

    override fun onError() {
        // Do nothing
    }

    override fun getSender(): String {
        return getLoggedInUser()
    }

    override fun getParticipants(): String {
        return intent.getStringExtra(BY_USERS_STRING)
    }

    private fun getLoggedInUser(): String {
        return mLoggedInUser
    }

    override fun onStop() {
        super.onStop()
        mMessageViewModel.dispose()
    }

    private var mChosenImage: Bitmap? = null
    override fun getImageToSend() {
        startImagePickerActivity()
    }

    private val captureImageRequest = 1111
    private fun startImagePickerActivity() {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, captureImageRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == captureImageRequest) {
            val extra = data?.extras ?: return
            val imageBitmap = extra["data"] as Bitmap
            mMessageViewModel.sendNewImageMessage(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}


