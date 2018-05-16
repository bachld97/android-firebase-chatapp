package com.example.cpu02351_local.firebasechatapp.ChatView.ContactList

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatView.MessageList.MessageListActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Conversation
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.databinding.ItemContactListBinding

class ContactListAdapter(private var mContacts: ArrayList<User>,
                         private var mRecyclerView: RecyclerView,
                         private var mChatViewModel: ChatViewModel)
    : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {


    private val itemClickListener = View.OnClickListener {
        val pos = mRecyclerView.getChildAdapterPosition(it)
        if (pos != RecyclerView.NO_POSITION) {
            val context = mRecyclerView.context
            val intent = Intent(context, MessageListActivity::class.java)
            val users = arrayOf(mChatViewModel.mLoggedInUser, mContacts[pos])
            intent.putExtra("conversationId", findConversationId(users))
            context.startActivity(intent)
        }
    }

    private fun findConversationId(users: Array<User>): String {
        return Conversation.uniqueId(users)
    }

    fun updateContacts(newList: List<User>) {
        mContacts.clear()
        mContacts.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactListBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(itemClickListener)
        return ContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mContacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(mContacts[position])
    }

    class ContactViewHolder(private val binding: ItemContactListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: User) {
            binding.user = contact
            binding.executePendingBindings()
        }
    }
}