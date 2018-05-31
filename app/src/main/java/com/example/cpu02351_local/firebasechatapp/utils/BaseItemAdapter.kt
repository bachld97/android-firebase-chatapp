package com.example.cpu02351_local.firebasechatapp.utils

import android.support.annotation.CallSuper
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class BaseItemAdapter<T : ListItem> : RecyclerView.Adapter<BaseItemHolder<out ListItem>>() {
    var listItems: List<ListItem>? = null
        private set

    var hasResultFromNetwork = false
        private set

    override fun getItemCount(): Int = listItems?.size ?: 0
    override fun onBindViewHolder(holder: BaseItemHolder<out ListItem>, position: Int) {
        listItems?.get(position)?.let { holder.bindItem(it) }
    }
    fun findItem(item: T): Int {
        val tem = listItems?.mapNotNull { it as? T }
        return tem?.indexOf(item) ?: -1
    }

    open fun calculateDiffResult(newItems: List<ListItem>?): DiffUtil.DiffResult? {
        if (newItems == null || listItems == null) return null
        val oldList = ArrayList(listItems)
        return DiffUtil.calculateDiff(BaseDiffCallback(oldList, newItems), true)
    }

    @CallSuper
    open fun setItems(items: List<ListItem>?, fromNetwork: Boolean) {
        val diffResult = calculateDiffResult(items)
        if (fromNetwork) {
            setNetworkItems(items, diffResult)
        } else {
            setLocalItems(items, diffResult)
        }
    }

    private fun setNetworkItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?) {
        hasResultFromNetwork = true
        if (this.listItems != null && diffResult != null) {
            this.listItems = items
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.listItems = items
            notifyDataSetChanged()
        }
    }

    private fun setLocalItems(items: List<ListItem>?, diffResult: DiffUtil.DiffResult?) {
        if (hasResultFromNetwork)
            return

        if (this.listItems != null && diffResult != null) {
            this.listItems = items
            diffResult.dispatchUpdatesTo(this)
        } else {
            this.listItems = items
            notifyDataSetChanged()
        }
    }

    fun resetState() {
        hasResultFromNetwork = false
    }

    class BaseDiffCallback(private val oldList : List<ListItem>,
                                   private val newList : List<ListItem>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].equalsItem(newList[newItemPosition])
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].equalsContent(newList[newItemPosition])
        }

    }
}