package com.example.cpu02351_local.firebasechatapp.ChatCore.model

class Conversation(val id: String) {
    var participantIds: List<String>? = null
    var messages: List<Message>? = null
    var createdTime: String? = null
}