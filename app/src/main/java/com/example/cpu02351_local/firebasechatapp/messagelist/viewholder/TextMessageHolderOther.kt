package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.AvatarViewModel
import com.example.cpu02351_local.firebasechatapp.model.Message

class TextMessageHolderOther(private val binding: ItemTextMessageFromOtherBinding): BaseMessageViewHolder(binding.root) {
    override fun bind(message: Message, showAva: Boolean, showTime: Boolean, avaUrl: String) {
        Log.d("DEBUGGING", avaUrl)

        binding.message = message

        if (binding.avaViewModel == null) {
            binding.avaViewModel = AvatarViewModel(showAva, avaUrl, showTime)
        } else {
            binding.avaViewModel!!.showTime = showTime
            binding.avaViewModel!!.showAva = showAva
            binding.avaViewModel!!.avaUrl = avaUrl
        }
        binding.invalidateAll()
        binding.executePendingBindings()
    }
}