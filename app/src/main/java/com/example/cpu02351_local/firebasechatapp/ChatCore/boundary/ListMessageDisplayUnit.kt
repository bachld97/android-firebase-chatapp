package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import io.reactivex.Scheduler

interface ListMessageDisplayUnit {
    fun displayThread() : Scheduler
    fun onDataLoaded(result: ArrayList<Message>)
    fun onDataError(errorMessage: String?)
}