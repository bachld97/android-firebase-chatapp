package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.databinding.ItemTextMessageFromOtherBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.InfoDisplayViewModel
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class TextMessageHolderOther(private val binding: ItemTextMessageFromOtherBinding): BaseMessageViewHolder(binding.root) {
    override fun bind(message: AbstractMessage, showAva: Boolean, showTime: Boolean, avaUrl: String, displayName: String) {
        Log.d("DEBUGGING", avaUrl)

        binding.message = message

        if (binding.infoViewModel == null) {
            binding.infoViewModel = InfoDisplayViewModel(showAva, avaUrl, showTime, displayName)
        } else {
            binding.infoViewModel!!.showTime = showTime
            binding.infoViewModel!!.showAva = showAva
            binding.infoViewModel!!.avaUrl = avaUrl
            binding.infoViewModel!!.displayName = displayName
        }
        binding.invalidateAll()
        binding.executePendingBindings()
    }
}