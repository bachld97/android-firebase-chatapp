package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import io.reactivex.Scheduler

interface ListConversationDisplayUnit {
    fun getDisplayThread() : Scheduler
    fun onSuccessfulLoadConversations(result: ArrayList<Conversation>)
    fun onFailLoadConversation(errorMessage: String?)
}