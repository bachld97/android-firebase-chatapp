package com.example.cpu02351_local.firebasechatapp.messagelist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.BaseMessageViewHolder
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.TextMessageHolder
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.TextMessageHolderOther
import com.example.cpu02351_local.firebasechatapp.model.Message

class MessageListAdapter(private val mMessages: ArrayList<Message>, private val loggedInUser: String)
    : RecyclerView.Adapter<BaseMessageViewHolder>() {

    companion object {
        const val MY_TEXT = 0
        const val OTHER_TEXT = 1
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
            notifyDataSetChanged()
        }
    }

    fun updateAvaMap(avaMap: HashMap<String, String>) {
        Log.d("DEBUGGING", avaMap.toString())
        this.avaMap = avaMap
    }
}