package com.example.cpu02351_local.firebasechatapp.messagelist.model

import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

class AudioMessageItem(message: AbstractMessage, shouldDisplaySenderInfo: Boolean, shouldDisplayTime: Boolean, fromThisUser: Boolean) :
        MessageItem(message, shouldDisplaySenderInfo, shouldDisplayTime, fromThisUser) {

    var isPlaying = false
    var currentPos = 0
}