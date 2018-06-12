package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemVideoMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.VideoMessageMineItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageVideoMineHolder(val binding: ItemVideoMessageBinding, private val imageClick: MessageItemAdapter.ItemClickCallback,
                             private val imageRetrySend: MessageItemAdapter.ItemClickCallback): BaseItemHolder<MessageItem>(binding.root) {

    override fun onBindItem(item: MessageItem) {
        if (binding.viewModel == null) {
            binding.viewModel = VideoMessageMineItemViewModel(item, imageClick, imageRetrySend)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }

}