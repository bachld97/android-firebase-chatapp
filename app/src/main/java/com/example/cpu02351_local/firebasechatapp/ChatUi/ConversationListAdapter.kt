package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationListBinding

class ConversationListAdapter(private val mConversations: ArrayList<Conversation>)
    : RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemConversationListBinding.inflate(layoutInflater, parent, false)
        return ConversationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mConversations.size
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(mConversations[position])
    }

    fun addOrUpdateConversation(newCon: Conversation) {
        if (!mConversations.contains(newCon)) {
            mConversations.add(0, newCon)
            notifyItemInserted(0)
        }
        else
            notifyItemChanged(mConversations.indexOf(newCon))
    }

    class ConversationViewHolder(private val binding: ItemConversationListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            binding.conversation = conversation
            binding.executePendingBindings()
        }
    }
}