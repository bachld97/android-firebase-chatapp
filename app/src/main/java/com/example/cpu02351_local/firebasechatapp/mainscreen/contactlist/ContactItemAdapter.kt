package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.databinding.ItemContactListBinding
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemAdapter
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder
import com.example.cpu02351_local.firebasechatapp.utils.ListItem

class ContactItemAdapter(contactItems: List<ContactItem>? = null) : BaseItemAdapter() {

    init {
        setItems(contactItems, null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItemHolder<out ListItem> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemContactListBinding.inflate(layoutInflater, parent, false)
        return ContactItemHolder(binding)
    }
}