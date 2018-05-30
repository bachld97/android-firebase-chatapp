package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationGroup3ListBinding
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class ConversationGroup3ItemHolder(private val binding: ItemConversationGroup3ListBinding)
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