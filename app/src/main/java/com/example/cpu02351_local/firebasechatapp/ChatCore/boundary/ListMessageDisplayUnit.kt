package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import io.reactivex.Scheduler

interface ListMessageDisplayUnit {
    fun getDisplayThread() : Scheduler
    fun onSuccessfulLoadMessage(result: ArrayList<Message>)
    fun onFailLoadMessage(errorMessage: String?)
}