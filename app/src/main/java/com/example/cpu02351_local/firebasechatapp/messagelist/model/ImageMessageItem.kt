package com.example.cpu02351_local.firebasechatapp.messagelist.model

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage

class ImageMessageItem(message: AbstractMessage, shouldDisplaySenderInfo: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean) :
    MessageItem(message, shouldDisplaySenderInfo, shouldDisplayTime, fromThisUser) {

    private val imageMessage = message as ImageMessage
    var width = imageMessage.width
    var height = imageMessage.height

    override fun diff(oldItem: MessageItem): MessageItem {
        if (oldItem is ImageMessageItem) {
            (message as ImageMessage).localUri = oldItem.imageMessage.localUri
            imageMessage.localUri = oldItem.imageMessage.localUri
        }
        message.content = oldItem.getContent()
        return this
    }
}