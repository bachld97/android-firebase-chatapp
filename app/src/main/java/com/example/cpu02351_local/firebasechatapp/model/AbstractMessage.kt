package com.example.cpu02351_local.firebasechatapp.model

import android.text.format.DateFormat

abstract class AbstractMessage {
    protected var id: String = ""
    protected var atTime: Long = -1L
    protected var content = ""

    protected var isFailed: Boolean = false
    protected var isSending: Boolean = false

    fun getDisplayTime(): String {
        return DateFormat.format("HH:mm", atTime).toString()
    }
    abstract fun getType(): String
    abstract fun getConversationoPreviewDisplay(): String
}