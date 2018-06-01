package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.MessageTextMineHolder
import com.example.cpu02351_local.firebasechatapp.messagelist.viewholder.MessageTextOtherHolder
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class MessageItemAdapter(context: Context)
    : BaseItemAdapter<MessageItem>() {
    private var infoMap: HashMap<String, User> ?= null

    companion object {
        const val TEXT_MINE = 0
        const val TEXT_OTHER = 1
    }

    fun updateUserInfo(info: HashMap<String, User> ) {
        infoMap = info
        listItems?.mapNotNull { it as? MessageItem }
                ?.forEachIndexed { index, item ->
                    if (item.hasUserInfoChange(info))
                        notifyItemChanged(index)
                }
    }


    override fun calculateDiffResult(newItems: List<ListItem>?): DiffUtil.DiffResult? {
        return null
    }

    override fun setItems(items: List<ListItem>?, fromNetwork: Boolean) {
        super.setItems(items, true)
        if (infoMap != null)
            updateUserInfo(infoMap!!)
    }

    override fun getItemViewType(position: Int): Int {
        return when ((listItems?.get(position) as? MessageItem)?.getType()) {
            "text_mine" -> TEXT_MINE
            "text_other" -> TEXT_OTHER
            else -> throw IllegalStateException("ListItems cannot be null at this time or unsupported messageType found [application is outdated]")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        lateinit var holder: BaseItemHolder<MessageItem>
        holder = when (viewType) {
            TEXT_MINE -> {
                val binding = ItemTextMessageBinding.inflate(layoutInflater, parent, false)
                MessageTextMineHolder(binding)
            }
            TEXT_OTHER -> {
                val binding = ItemTextMessageFromOtherBinding.inflate(layoutInflater, parent, false)
                MessageTextOtherHolder(binding)
            }
            else -> throw RuntimeException("Unsupported viewType")
        }
        return holder
    }


}