package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.AvatarViewModel
import com.example.cpu02351_local.firebasechatapp.model.Message

class TextMessageHolder(private val binding: ItemTextMessageBinding): BaseMessageViewHolder(binding.root){
    override fun bind(message: Message, showAva: Boolean, avaUrl: String) {
        binding.message = message

        if (binding.avaViewModel == null) {
            binding.avaViewModel = AvatarViewModel(showAva, avaUrl)
        } else {
            binding.avaViewModel!!.showAva = showAva
            binding.avaViewModel!!.avaUrl = avaUrl
        }
        binding.invalidateAll()
        binding.executePendingBindings()
    }
}