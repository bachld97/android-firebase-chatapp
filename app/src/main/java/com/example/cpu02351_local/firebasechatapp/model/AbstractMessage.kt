package com.example.cpu02351_local.firebasechatapp.model

import android.text.format.DateFormat

abstract class AbstractMessage(var id: String = "",
                               var atTime: Long = -1L,
                               var byUser: String = "",
                               var content: String = "") {

    open fun buildAdditionalContent() : HashMap<String, String>? {
        return null
    }

    var isFailed: Boolean = false
    var isSending: Boolean = false

    fun getDisplayTime(): String {
        return DateFormat.format("HH:mm", atTime).toString()
    }
    abstract fun getType(): String
    abstract fun getConversationPreviewDisplay(): String

    override fun equals(other: Any?): Boolean {
        return other is AbstractMessage && other.id == id
    }
}