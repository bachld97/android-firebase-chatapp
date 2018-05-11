package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import com.example.cpu02351_local.firebasechatapp.ChatCore.model.User
import io.reactivex.Scheduler
import java.util.ArrayList

interface ListContactDisplayUnit {
    fun displayThread() : Scheduler
    fun onDataLoaded(result: ArrayList<User>)
    fun onDataError(message: String?)
}