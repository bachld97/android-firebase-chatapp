package com.example.cpu02351_local.firebasechatapp.utils

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseItemAdapter<T : ListItem> : RecyclerView.Adapter<BaseItemHolder<out ListItem>>() {
    var items: List<ListItem>? = null
        private set

    var hasResultFromNetwork = false
        private set

    override fun getItemCount(): Int = items?.size ?: 0
    override fun onBindViewHolder(holder: BaseItemHolder<out ListItem>, position: Int) {
        items?.get(position)?.let { holder.bindItem(it) }
    }

    fun findItem(item: T): Int {
        val tem = items?.map { it as T }
        return if (tem == null)
            -1
        else
            tem.indexOf(item)
    }

    fun setItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?, fromNetwork: Boolean) {
        if (fromNetwork) {
            setNetworkItems(items, diffResult)
        } else {
            setLocalItems(items, diffResult)
        }
    }

    private fun setNetworkItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?) {
        hasResultFromNetwork = true
        if (this.items != null && diffResult != null) {
            this.items = items
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.items = items
            notifyDataSetChanged()
        }
    }

    private fun setLocalItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?) {
        if (hasResultFromNetwork)
            return

        if (this.items != null && diffResult != null) {
            this.items = items
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.items = items
            notifyDataSetChanged()
        }
    }


}