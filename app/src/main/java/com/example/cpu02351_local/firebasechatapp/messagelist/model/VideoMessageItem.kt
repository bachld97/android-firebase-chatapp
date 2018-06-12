package com.example.cpu02351_local.firebasechatapp.messagelist.model

import com.example.cpu02351_local.firebasechatapp.model.messagetypes.VideoMessage

class VideoMessageItem(private val videoMessage: VideoMessage, shouldDisplaySenderInfo: Boolean,
                       shouldDisplayTime: Boolean, fromThisUser: Boolean)
    : MessageItem(videoMessage, shouldDisplaySenderInfo, shouldDisplayTime, fromThisUser) {

    var width = videoMessage.width
    var height = videoMessage.height
    var videoUrl = videoMessage.content
    var thumbnailUrl = videoMessage.thumbnailLink
}
