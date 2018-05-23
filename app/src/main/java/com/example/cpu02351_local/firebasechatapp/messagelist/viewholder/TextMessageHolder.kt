package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.support.v7.widget.RecyclerView
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.model.Message

class TextMessageHolder(private val binding: ItemTextMessageBinding): BaseMessageViewHolder(binding.root){
    override fun bind(message: Message, showAva: Boolean) {
        binding.message = message
        binding.executePendingBindings()
    }
}