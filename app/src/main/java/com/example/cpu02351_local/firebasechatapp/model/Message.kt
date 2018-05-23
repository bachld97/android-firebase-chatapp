package com.example.cpu02351_local.firebasechatapp.model

import android.text.format.DateFormat

class Message(val id: String) {

    var byUser: String? = null // Default = getCurrent  User
    var atTime = System.currentTimeMillis()
    var content = "Hello world"

    override fun equals(other: Any?): Boolean {
        return other is Message && other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (byUser?.hashCode() ?: 0)
        result = 31 * result + atTime.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }

    fun getType() : String = "text"
    fun getTime(): String {
        return DateFormat.format("HH:mm", atTime).toString()
    }
}