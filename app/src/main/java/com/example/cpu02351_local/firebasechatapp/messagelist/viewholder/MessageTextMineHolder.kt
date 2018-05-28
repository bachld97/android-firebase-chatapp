package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class MessageTextMineHolder(private val binding: ItemTextMessageBinding) : MessageItemHolder(binding.root) {
    override fun onBindItem(item: MessageItem) {
        super.onBindItem(item)
        // binding.message = item
        binding.executePendingBindings()
    }
}