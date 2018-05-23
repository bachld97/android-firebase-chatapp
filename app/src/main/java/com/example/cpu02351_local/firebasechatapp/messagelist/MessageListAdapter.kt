package com.example.cpu02351_local.firebasechatapp.messagelist

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.BaseMessageViewHolder
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.TextMessageHolder
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.TextMessageHolderOther
import com.example.cpu02351_local.firebasechatapp.model.Message

class MessageListAdapter(private val mMessages: ArrayList<Message>,
                         private val loggedInUser: String,
                         private val mRecyclerView: RecyclerView,
                         private val endlessLoader: EndlessLoader)
    : RecyclerView.Adapter<BaseMessageViewHolder>() {

    companion object {
        const val MY_TEXT = 0
        const val OTHER_TEXT = 1
    }

    private var isScrolling = false
    private var isLoading = false
    private val loadThreshold = 2

    init {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItemPos = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                Log.d("LAST_ITEM", lastItemPos.toString())
                if (!isLoading && isScrolling && mMessages.size - lastItemPos <= loadThreshold) {
                    isLoading = true
                    endlessLoader.loadMore()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = (newState == SCROLL_STATE_TOUCH_SCROLL)
            }
        })
    }
    private var avaMap: HashMap<String, String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)

        return when (viewType) {
            MY_TEXT -> TextMessageHolder(ItemTextMessageBinding.inflate(layoutInflater, parent, false))
            OTHER_TEXT -> TextMessageHolderOther(ItemTextMessageFromOtherBinding.inflate(layoutInflater, parent, false))
            else -> throw IllegalStateException()
        }
    }

    fun clear() {
        mMessages.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mMessages[position].byUser == loggedInUser)
            MY_TEXT
        else
            OTHER_TEXT
    }

    override fun getItemCount(): Int {
       return mMessages.size
    }

    override fun onBindViewHolder(holder: BaseMessageViewHolder, position: Int) {
        holder.bind(mMessages[position], shouldShowAva(position), avaMap?.get(mMessages[position].byUser) ?: "")
    }

    private fun shouldShowAva(pos: Int): Boolean {
        return pos == mMessages.size - 1 || mMessages[pos + 1].byUser != mMessages[pos].byUser
    }

    fun updateList(result: List<Message>) {
        val oldSize = mMessages.size
        mMessages.clear()
        mMessages.addAll(result.reversed())
        notifyItemRangeInserted(0, mMessages.size - oldSize)
    }

    fun addMessage(message: Message) {
        if (!mMessages.contains(message)) {
            mMessages.add(0, message)
            notifyItemInserted(0)
        } else {
            isLoading = false
            mMessages.removeAt(mMessages.size - 1)
            mMessages.add(0, message)
            notifyItemMoved(mMessages.size - 1, 0)
        }
    }

    fun updateAvaMap(avaMap: HashMap<String, String>) {
        Log.d("DEBUGGING", avaMap.toString())
        this.avaMap = avaMap
    }
}