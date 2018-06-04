package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemImageMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.MessageImageMineItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageImageMineHolder(val binding: ItemImageMessageBinding, private val imageClick: MessageItemAdapter.ItemClickCallback): BaseItemHolder<MessageItem>(binding.root) {

    override fun onBindItem(item: MessageItem) {
        if (binding.viewModel == null) {
            binding.viewModel = MessageImageMineItemViewModel(item, binding.root, imageClick)
        } else {
            binding.viewModel!!.messageItem = item
            binding.viewModel!!.v = binding.root
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }

}