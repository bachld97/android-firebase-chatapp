package com.example.cpu02351_local.firebasechatapp.ChatViewModel.model

data class User(val id: String, var name: String = "", var conversations: String = ""): Comparable<User> {
    override fun compareTo(other: User): Int {
        return id.compareTo(other.id)
    }

    override fun toString(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return other is User && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}