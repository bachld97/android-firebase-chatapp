package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationList2Binding
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ConversationItemAdapter(private val mRecyclerView: RecyclerView,
                              private val mViewModel: ConversationViewModel)
    : BaseItemAdapter<ConversationItem>() {

    private val itemClickListener = View.OnClickListener {
        val pos = mRecyclerView.getChildAdapterPosition(it)
        if (pos != RecyclerView.NO_POSITION && listItems != null) {
            if (listItems != null) {
                mViewModel.onConversationClicked(listItems!![pos] as ConversationItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemConversationList2Binding.inflate(layoutInflater, parent, false)
        binding.root.setOnClickListener(itemClickListener)
        return ConversationItemHolder(binding)
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