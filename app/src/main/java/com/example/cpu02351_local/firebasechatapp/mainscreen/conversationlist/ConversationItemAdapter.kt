package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationGroup3ListBinding
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationGroup4ListBinding
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationSingleListBinding
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ConversationItemAdapter(private val mRecyclerView: RecyclerView,
                              private val mViewModel: ConversationViewModel)
    : BaseItemAdapter<ConversationItem>() {

    companion object {
        const val SINGLE_CONVERSATION = 1
        const val GROUP3_CONVERSATION = 2
        const val GROUP4_CONVERSATION = 3
    }

    private val itemClickListener = View.OnClickListener {
        val pos = mRecyclerView.getChildAdapterPosition(it)
        if (pos != RecyclerView.NO_POSITION && listItems != null) {
            if (listItems != null) {
                mViewModel.onConversationClicked(listItems!![pos] as ConversationItem)
            }
        }
    }

    // potential error when size = 0
    override fun getItemViewType(position: Int): Int {
        return when ((listItems?.get(position) as? ConversationItem)?.size) {
            null -> throw RuntimeException("Unsupported view type")
            2 -> SINGLE_CONVERSATION
            3 -> GROUP3_CONVERSATION
            else -> GROUP4_CONVERSATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)

        lateinit var holder: BaseItemHolder<ConversationItem>
        holder = when (viewType) {
            SINGLE_CONVERSATION -> {
                val binding = ItemConversationSingleListBinding.inflate(layoutInflater, parent, false)
                ConversationItemHolder(binding)
            }
            GROUP3_CONVERSATION -> {
                val binding = ItemConversationGroup3ListBinding.inflate(layoutInflater, parent, false)
                ConversationGroup3ItemHolder(binding)
            }
            GROUP4_CONVERSATION -> {
                val binding = ItemConversationGroup4ListBinding.inflate(layoutInflater, parent, false)
                ConversationGroup4ItemHolder(binding)
            }
            else -> throw RuntimeException("Unsupported viewType")
        }

        holder.setOnClick(itemClickListener)
        return holder
    }

    override fun setItems(items: List<ListItem>?, fromNetwork: Boolean) {
        super.setItems(items, fromNetwork)
        if (infoMap != null)
            updateUserInfo(infoMap!!)
    }

    private var infoMap: HashMap<String, User> ?= null
    fun updateUserInfo(info: HashMap<String, User>) {
        infoMap = info
        listItems?.mapNotNull { it as? ConversationItem }
                ?.forEach{ item ->
                    val pos = findItem(item)
                    if (pos != -1) {
                        item.computeDisplayInfo(info)
                        notifyItemChanged(pos)
                    }
                }
    }
}