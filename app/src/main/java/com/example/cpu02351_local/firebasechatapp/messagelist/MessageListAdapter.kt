package com.example.cpu02351_local.firebasechatapp.messagelist

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.Message
import com.example.cpu02351_local.firebasechatapp.databinding.ItemMessageListBinding

class MessageListAdapter(private val mMessages: ArrayList<Message>, private val loggedInUser: String)
    : RecyclerView.Adapter<MessageListAdapter.MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding = ItemMessageListBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return mMessages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(mMessages[position], loggedInUser)
    }

    fun updateList(result: List<Message>) {
        val oldSize = mMessages.size
        mMessages.clear()
        mMessages.addAll(result.reversed())
        notifyItemRangeInserted(0, mMessages.size - oldSize)
    }

    fun addMessage(message: Message) {
        mMessages.add(message)
        notifyDataSetChanged()
        // notifyItemInserted(mMessages.size - 1)
    }

    class MessageViewHolder(private val binding: ItemMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, loggedInUser: String) {
            binding.message = message
            binding.executePendingBindings()

            // Mock feature - other users
            if (message.byUser != loggedInUser) {
                binding.root.setBackgroundColor(Color.MAGENTA)
            }
        }
    }
}