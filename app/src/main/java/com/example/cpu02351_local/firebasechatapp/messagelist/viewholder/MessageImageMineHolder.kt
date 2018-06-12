package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.widget.ImageView
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ItemImageMessageBinding
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel.ImageMessageMineItemViewModel
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class MessageImageMineHolder(val binding: ItemImageMessageBinding, private val imageClick: MessageItemAdapter.ItemClickCallback,
                             private val imageRetrySend: MessageItemAdapter.ItemClickCallback): BaseItemHolder<MessageItem>(binding.root) {

    override fun onBindItem(item: MessageItem) {
        val imageItem = ImageMessageItem(item.message, item.shouldDisplaySenderInfo, item.shouldDisplayTime, item.fromThisUser)

        if (binding.viewModel == null) {
            binding.viewModel = ImageMessageMineItemViewModel(imageItem, binding.root, imageClick, imageRetrySend)
        } else {
            binding.viewModel!!.messageItem = imageItem
            binding.viewModel!!.v = binding.root
            binding.invalidateAll()
        }
        binding.executePendingBindings()
        val b = (imageItem.message as ImageMessage).bitmap
        if (b != null) {
            binding.root.findViewById<ImageView>(R.id.image_display).setImageBitmap(b)
        }
    }

}