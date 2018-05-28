package com.example.cpu02351_local.firebasechatapp.utils

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseItemHolder<T : ListItem>(v: View) : RecyclerView.ViewHolder(v) {

    fun bindItem(item: ListItem) {
        onBindItem(item as T)
    }

    abstract fun onBindItem(item: T)
}