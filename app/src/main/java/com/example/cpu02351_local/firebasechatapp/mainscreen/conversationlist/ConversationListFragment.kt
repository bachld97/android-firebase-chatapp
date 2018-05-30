package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import com.example.cpu02351_local.firebasechatapp.utils.ConversationListDivider
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ConversationListFragment :
        ConversationView,
        Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(userId: String): ConversationListFragment {
            val temp = ConversationListFragment()
            val args = Bundle()
            args.putString("userId", userId)
            temp.arguments = args
            return temp
        }
    }

    private lateinit var userId: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ConversationItemAdapter
    private var mConversationLoader: ConversationLoader = FirebaseConversationLoader()
    private lateinit var mConversationViewModel: ConversationViewModel

    @Inject lateinit var localDatabase: RoomLocalDatabase

    private fun init() {
        mConversationViewModel = ConversationViewModel(mConversationLoader, this, userId)
        mAdapter = ConversationItemAdapter(mRecyclerView, mConversationViewModel)
        mRecyclerView.adapter = mAdapter

        DaggerRoomLocalDatabaseComponent
                .builder()
                .contextModule(ContextModule(context!!))
                .build()
                .injectInto(this)
        loadUsers()

        mConversationViewModel.setLocalDatabase(localDatabase)
    }

    private fun loadUsers() {
        localDatabase.loadUserAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { res ->
                    val userMap = HashMap<String, User>()
                    res.forEach {
                        userMap[it.id] = it
                    }
                    mAdapter.updateUserInfo(userMap)
                }
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        userId = args?.get("userId") as String
    }

    private fun dispose() {
        mConversationViewModel.dispose()
    }

    override fun onLocalConversationsLoaded(result: List<ConversationItem>) {
        mAdapter.setItems(result, false)
    }
    override fun onConversationsLoaded(result: List<ConversationItem>) {
        mAdapter.setItems(result, true)
    }

    override fun navigate(where: ConversationItem) {
        val conversation = where.getConversation()
        val intent = MessageListActivity.newInstance(
                context!!, conversation.id, conversation.participantIds.joinToString(Conversation.ID_DELIM))
        context!!.startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_conversation_list, container, false)
        mRecyclerView = root.findViewById(R.id.conversationListContainer)
        mRecyclerView.addItemDecoration(ConversationListDivider(context!!))
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        init()
        return root
    }

    override fun onStart() {
        super.onStart()
        mConversationViewModel.resume()
        mAdapter.resetState()
    }

    override fun onStop() {
        super.onStop()
        dispose()
    }
}