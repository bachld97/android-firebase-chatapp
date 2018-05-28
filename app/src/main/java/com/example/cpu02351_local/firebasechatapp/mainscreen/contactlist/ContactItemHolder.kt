package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.databinding.ItemContactListBinding
import com.example.cpu02351_local.firebasechatapp.utils.BaseItemHolder

class ContactItemHolder(val binding: ItemContactListBinding) :
        BaseItemHolder<ContactItem>(binding.root) {

    override fun onBindItem(item: ContactItem) {
        if (binding.viewModel == null) {
            binding.viewModel = ContactItemViewModel(item)
        } else {
            binding.viewModel!!.contactItem = item
            binding.invalidateAll()
        }
        binding.executePendingBindings()
    }

}