package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.support.v7.widget.RecyclerView
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.model.Message

class TextMessageHolderOther(private val binding: ItemTextMessageFromOtherBinding): BaseMessageViewHolder(binding.root) {
    override fun bind(message: Message, showAva: Boolean) {
        binding.message = message
        binding.executePendingBindings()
    }
}