package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemImageMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.ImageMessageOtherItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageImageOtherHolder(val binding: ItemImageMessageFromOtherBinding, private val imageClick: MessageItemAdapter.ItemClickCallback): BaseItemHolder<MessageItem>(binding.root) {

    override fun onBindItem(item: MessageItem) {
        val imageItem = ImageMessageItem(item.message, item.shouldDisplaySenderInfo, item.shouldDisplayTime, item.fromThisUser)
        if (binding.viewModel == null) {
            binding.viewModel = ImageMessageOtherItemViewModel(imageItem, binding.root, imageClick)
        } else {
            binding.viewModel!!.messageItem = imageItem
            binding.viewModel!!.v = binding.root
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }

}
