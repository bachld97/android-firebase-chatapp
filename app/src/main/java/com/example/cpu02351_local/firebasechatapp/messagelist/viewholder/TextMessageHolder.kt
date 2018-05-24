package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.InfoDisplayViewModel
import com.example.cpu02351_local.firebasechatapp.model.Message

class TextMessageHolder(private val binding: ItemTextMessageBinding): BaseMessageViewHolder(binding.root){
    override fun bind(message: Message, showAva: Boolean, showTime: Boolean, avaUrl: String, displayName: String) {
        binding.message = message

        if (binding.infoViewModel == null) {
            binding.infoViewModel = InfoDisplayViewModel(showAva, avaUrl, showTime, displayName)
        } else {
            binding.infoViewModel!!.showAva = showAva
            binding.infoViewModel!!.showTime = showTime
            binding.infoViewModel!!.avaUrl = avaUrl
            binding.infoViewModel!!.displayName = displayName
        }
        binding.invalidateAll()
        binding.executePendingBindings()
    }
}