package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemContactListBinding
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem
import java.util.*

class ContactItemAdapter(private val mContactViewModel: ContactViewModel,
                         private val mRecyclerView: RecyclerView) : BaseItemAdapter<ContactItem>() {

    private val itemClickListener = View.OnClickListener {
        val pos = mRecyclerView.getChildAdapterPosition(it)
        if (pos != RecyclerView.NO_POSITION && listItems != null) {
            if (selectedItems.size != 0) {
                itemSelectedCallback.onItemSelected(listItems!![pos] as ContactItem)
            } else {
                mContactViewModel.onSelectedContactsAction(
                        arrayListOf(listItems!![pos]).map { it as ContactItem })
            }
        }
    }

    private val selectedItems = arrayListOf<Int>()

    private val itemSelectedCallback = object : ItemSelectedCallback {
        override fun onItemSelected(contactItem: ContactItem) {
            val pos = findItem(contactItem)
            if (pos == -1)
                return
            else {
                if (selectedItems.contains(pos)) {
                    selectedItems.remove(pos)
                    (listItems!![pos] as ContactItem).isSelected = false
                }
                else {
                    (listItems!![pos] as ContactItem).isSelected= true
                    selectedItems.add(pos)
                }
                notifyItemChanged(pos)
            }

            if (listItems != null) {
                val selectedMess = selectedItems.map { listItems!![it] as ContactItem }
                mContactViewModel.onSelectedContactChanged(selectedItems, selectedMess)
            }
        }
    }

    fun clearSelected() {
        selectedItems.forEach {
            (listItems?.get(it) as ContactItem).isSelected = false
        }
        selectedItems.clear()
        notifyDataSetChanged()
        mContactViewModel.onSelectedContactChanged(selectedItems, Collections.emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactListBinding.inflate(layoutInflater, parent, false)
        binding.root.setOnClickListener(itemClickListener)
        return ContactItemHolder(binding, itemSelectedCallback)
    }

    interface ItemSelectedCallback {
        fun onItemSelected(contactItem: ContactItem)
    }
}