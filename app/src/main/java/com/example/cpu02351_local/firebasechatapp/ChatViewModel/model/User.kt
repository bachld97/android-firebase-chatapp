package com.example.cpu02351_local.firebasechatapp.ChatViewModel.model

data class User(val id: String, var name: String = "", var conversations: String = "") {
    override fun equals(other: Any?): Boolean {
        return other is User && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}