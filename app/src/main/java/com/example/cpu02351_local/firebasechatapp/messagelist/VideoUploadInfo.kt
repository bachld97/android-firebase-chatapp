package com.example.cpu02351_local.firebasechatapp.messagelist

import java.io.Serializable

class VideoUploadInfo(val filePath: String, val messageId: String,
                      val senderId: String, val conversationId: String) : Serializable {

    var byUsers = mutableListOf<String>()
    var videoWidth = 0
    var videoHeight = 0
    var thumbnailLink = ""
    var videoLink = ""
}