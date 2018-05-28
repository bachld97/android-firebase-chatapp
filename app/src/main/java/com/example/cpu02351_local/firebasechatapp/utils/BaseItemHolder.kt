package com.example.cpu02351_local.firebasechatapp.utils

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View

open class BaseItemHolder<T : ListItem>(v: View) : RecyclerView.ViewHolder(v) {
    var item: T? = null
        private set

    fun bindItem(item: ListItem) {
        onBindItem(item as T)
    }

    @CallSuper
    open fun onBindItem(item: T) {
        this.item = item
    }
}