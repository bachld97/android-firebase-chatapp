package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import io.reactivex.Scheduler

interface ListConversationDisplayUnit {
    fun displayThread() : Scheduler
    fun onDataLoaded(result: ArrayList<Conversation>)
    fun onDataError(errorMessage: String?)
}