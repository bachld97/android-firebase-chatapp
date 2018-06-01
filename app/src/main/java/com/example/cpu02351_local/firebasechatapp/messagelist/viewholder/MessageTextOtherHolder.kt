package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.TextMessageOtherItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageTextOtherHolder(private val binding: ItemTextMessageFromOtherBinding) : BaseItemHolder<MessageItem>(binding.root) {
    override fun onBindItem(item: MessageItem) {
        // binding.message = item
        if (binding.viewModel == null) {
            binding.viewModel = TextMessageOtherItemViewModel(item)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }
}