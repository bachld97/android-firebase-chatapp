package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemAudioMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.AudioMessageOtherItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageAudioOtherHolder(private val binding: ItemAudioMessageFromOtherBinding,
                              private val messageClick: MessageItemAdapter.ItemClickCallback) : BaseItemHolder<MessageItem>(binding.root) {
    override fun onBindItem(item: MessageItem) {
        // binding.message = item
        if (binding.viewModel == null) {
            binding.viewModel = AudioMessageOtherItemViewModel(item, messageClick)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }
}

