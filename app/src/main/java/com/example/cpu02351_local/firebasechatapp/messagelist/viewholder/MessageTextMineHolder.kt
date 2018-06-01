package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.TextMessageMineItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageTextMineHolder(private val binding: ItemTextMessageBinding,
                            private val messageClick: MessageItemAdapter.ItemClickCallback,
                            private val messageLongClick: MessageItemAdapter.ItemLongClickCallback) : BaseItemHolder<MessageItem>(binding.root) {
    override fun onBindItem(item: MessageItem) {
        if (binding.viewModel == null) {
            binding.viewModel = TextMessageMineItemViewModel(item, messageClick, messageLongClick)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }
}