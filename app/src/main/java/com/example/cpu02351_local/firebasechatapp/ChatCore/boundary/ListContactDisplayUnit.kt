package com.example.cpu02351_local.firebasechatapp.ChatCore.boundary

import io.reactivex.Scheduler

interface ListContactDisplayUnit {
    fun displayThread() : Scheduler
    fun onDataLoaded()
    fun onDataError()
}