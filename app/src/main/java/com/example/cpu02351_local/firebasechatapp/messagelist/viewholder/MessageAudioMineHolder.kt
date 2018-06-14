package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemAudioMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.AudioMessageMineItemViewModel
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageAudioMineHolder(private val binding: ItemAudioMessageBinding,
                             private val messageClick: MessageItemAdapter.ItemClickCallback) : BaseItemHolder<MessageItem>(binding.root) {
    override fun onBindItem(item: MessageItem) {
        if (binding.viewModel == null) {
            binding.viewModel = AudioMessageMineItemViewModel(item, messageClick)
        } else {
            binding.viewModel!!.messageItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }
}
