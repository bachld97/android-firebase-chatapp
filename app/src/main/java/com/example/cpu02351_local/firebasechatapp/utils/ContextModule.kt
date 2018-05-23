package com.example.cpu02351_local.firebasechatapp.utils

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {
    @Provides
    fun getContext() = context.applicationContext
}