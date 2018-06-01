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
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast


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

    private val textLongClicked = object : ItemLongClickCallback {
        override fun onLongClick(item: MessageItem) {
            Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(null, item.getContent())
            clipboard.primaryClip = clip
        }
    }

    private val messageClick = object : ItemClickCallback {
        override fun onClick(item: MessageItem) {
            val pos = findItem(item)
            if (pos != -1) {
                item.shouldDisplayTime = !item.shouldDisplayTime
                notifyItemChanged(pos)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        lateinit var holder: BaseItemHolder<MessageItem>
        holder = when (viewType) {
            TEXT_MINE -> {
                val binding = ItemTextMessageBinding.inflate(layoutInflater, parent, false)
                MessageTextMineHolder(binding, messageClick, textLongClicked)
            }
            TEXT_OTHER -> {
                val binding = ItemTextMessageFromOtherBinding.inflate(layoutInflater, parent, false)
                MessageTextOtherHolder(binding, messageClick, textLongClicked)
            }
            else -> throw RuntimeException("Unsupported viewType")
        }
        return holder
    }

    interface ItemLongClickCallback {
        fun onLongClick(item: MessageItem)
    }

    interface ItemClickCallback {
        fun onClick(item: MessageItem)
    }
}