package com.example.cpu02351_local.firebasechatapp.ChatUi.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Message
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationBinding

class ConversationAdapter(private val mMessages: ArrayList<Message>, private val mRecyclerView: RecyclerView)
    : RecyclerView.Adapter<ConversationAdapter.MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding = ItemConversationBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return mMessages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(mMessages[position])
    }

    fun updateList(result: ArrayList<Message>) {
        val oldSize = mMessages.size
        mMessages.clear()
        mMessages.addAll(result.reversed())
        notifyItemRangeInserted(0, mMessages.size - oldSize)
        // notifyDataSetChanged()
    }

    class MessageViewHolder(private val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
            binding.executePendingBindings()
        }
    }
}