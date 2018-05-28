package com.example.cpu02351_local.firebasechatapp.utils

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseItemAdapter : RecyclerView.Adapter<BaseItemHolder<out ListItem>>() {
    var items: List<ListItem>? = null
        private set

    override fun getItemCount(): Int = items?.size ?: 0
    override fun onBindViewHolder(holder: BaseItemHolder<out ListItem>, position: Int) {
        items?.get(position)?.let { holder.bindItem(it) }
    }

    fun setItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?) {
        if (this.items != null && diffResult != null) {
            this.items = items
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.items = items
            notifyDataSetChanged()
        }
    }
}