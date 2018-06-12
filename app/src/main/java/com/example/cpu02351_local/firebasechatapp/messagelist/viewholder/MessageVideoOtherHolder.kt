package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemVideoMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.VideoMessageOtherItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageVideoOtherHolder(val binding: ItemVideoMessageFromOtherBinding, private val imageClick: MessageItemAdapter.ItemClickCallback): BaseItemHolder<MessageItem>(binding.root) {

    override fun onBindItem(item: MessageItem) {
        if (binding.viewModel == null) {
            binding.viewModel = VideoMessageOtherItemViewModel(item, imageClick)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }

}
