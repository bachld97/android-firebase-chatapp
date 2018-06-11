package com.example.cpu02351_local.firebasechatapp.messagelist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ActivityMessageListBinding
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.loginscreen.LogInHelper
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_message_list.*
import java.io.File
import javax.inject.Inject

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
        const val STORAGE_PERM = 111

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

    override fun startUploadService(info: VideoUploadInfo) {
        val service = UploadVideoService.newInstance(this, info)
        startService(service)
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
        mAdapter2 = MessageItemAdapter(this, mMessageViewModel)
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
                    res.forEach {
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

    override fun onRestart() {
        super.onRestart()
        mMessageViewModel.resume()
    }

    override fun onStop() {
        super.onStop()
        mMessageViewModel.dispose()
    }

    private lateinit var mMessageId: String

    override fun getImageToSend(messageId: String) {
        mMessageId = messageId
        startImagePickerActivity()
    }

    override fun getVideoToSend(messageId: String) {
        mMessageId = messageId
        startVideoPickerActivity()
    }

    private val pickVideoRequest = 1212
    private fun startVideoPickerActivity() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, pickVideoRequest)
    }

    private val captureImageRequest = 1111
    private fun startImagePickerActivity() {
        val file = try {
               createImageFile(mMessageId)
        } catch (exception: Exception) {
            null
        } ?: return
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val uri = FileProvider.getUriForFile( this,
                    "com.example.firebasechatapp.fileprovider",
                    file)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(takePictureIntent, captureImageRequest)
        }
    }

    private var mPhoto: File? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK)
            return super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            captureImageRequest -> {
                mMessageViewModel.sendImageMessageWithUri(Uri.fromFile(mPhoto), mMessageId)
            }
            pickVideoRequest -> {
                val uri = data?.data ?: return
                val path = getPathFromUri(uri) ?: return
                mMessageViewModel.sendVideoMessageWithPath(path, mMessageId)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getPathFromUri(uri: Uri) : String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            path = cursor.getString(column_index)
        }
        cursor.close()
        return path
    }


    override fun createImageFile(messageId: String): File? {
        var imageFile: File? = null
        if (shouldAskPermission()) {
            askPermission()
        } else {
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            imageFile = File.createTempFile(
                    messageId,
                    ".jpg",
                    storageDir)
        }

        mPhoto = imageFile
        Log.d("DEBUG_PATH", imageFile?.absolutePath)
        return imageFile
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERM)
    }

    private fun shouldAskPermission(): Boolean {
        return ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERM)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImagePickerActivity()
            }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}


