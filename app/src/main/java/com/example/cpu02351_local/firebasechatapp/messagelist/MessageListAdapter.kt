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
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class MessageListAdapter(private val mMessages: ArrayList<AbstractMessage>,
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
    private val loadThreshold = 3

    init {
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItemPos = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (hasResultFromServer && !isLoading &&
                        isScrolling && mMessages.size - lastItemPos <= loadThreshold) {
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
    private var nameMap: HashMap<String, String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        throw IllegalStateException()
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
        holder.bind(mMessages[position], shouldShowAva(position),
                shouldShowTime(position),
                avaMap?.get(mMessages[position].byUser) ?: "",
                nameMap?.get(mMessages[position].byUser) ?: mMessages[position].byUser!!)
    }

    private fun shouldShowAva(pos: Int): Boolean {
        return pos == mMessages.size - 1 || mMessages[pos + 1].byUser != mMessages[pos].byUser
    }

    private fun shouldShowTime(pos: Int): Boolean {
        return pos == 0 || mMessages[pos - 1].byUser != mMessages[pos].byUser
    }

    private var hasResultFromServer = false
    fun updateFromLocal(result: List<AbstractMessage>) {
        if (!hasResultFromServer) {
            synchronized(mMessages) {
                mMessages.clear()
                mMessages.addAll(result)
                notifyItemRangeInserted(0, mMessages.size)
            }
        }
    }

    fun updateFromNetwork(result: List<AbstractMessage>) {
        hasResultFromServer = true
        synchronized(mMessages) {
            // val old = mMessages.subList(0, commonPoint)
            mMessages.clear()
            // mMessages.addAll(old)
            mMessages.addAll(result.reversed())
            // notifyItemRangeRemoved(0, commonPoint)
            notifyItemRangeInserted(0, result.size)
            notifyDataSetChanged()
        }
    }

    fun addMessage(message: AbstractMessage) {
        val oldPos = mMessages.indexOf(message)
        if (oldPos == -1) {
            mMessages.add(0, message)
            notifyItemChanged(0)
            notifyItemInserted(0)
            mRecyclerView.smoothScrollToPosition(0)
        } else {
            mMessages[oldPos] = message
            notifyItemChanged(oldPos)
        }
    }

    fun updateInfoMaps(avaMap: HashMap<String, String>, nameMap: HashMap<String, String>) {
        Log.d("DEBUGGING", avaMap.toString())
        this.avaMap = avaMap
        this.nameMap = nameMap
    }

    fun addLoadMoreMessages(moreMessages: List<AbstractMessage>) {
        val oldSize = mMessages.size - 1
        mMessages.addAll(moreMessages.reversed())
        notifyItemRangeInserted(oldSize + 1, moreMessages.size)
        isLoading = false
    }
}