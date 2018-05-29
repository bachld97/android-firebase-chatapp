package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationList2Binding
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class ConversationItemHolder(private val binding: ItemConversationList2Binding)
    : BaseItemHolder<ConversationItem>(binding.root) {

    override fun onBindItem(item: ConversationItem) {
        if (binding.viewModel == null) {
            binding.viewModel = ConversationItemViewModel(item)
        } else {
            binding.viewModel!!.item = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }
}