package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.content.Intent
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cpu02351_local.firebasechatapp.ChatCore.model.Conversation
import com.example.cpu02351_local.firebasechatapp.databinding.ItemConversationListBinding


class ConversationListAdapter(private val mConversations: ArrayList<Conversation>, private val mRecyclerView: RecyclerView)
    : RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder>() {

    private val mItemAction: View.OnClickListener? = View.OnClickListener { v ->
        val pos = mRecyclerView.getChildAdapterPosition(v)
        if (pos != RecyclerView.NO_POSITION) {
            val context = mRecyclerView.context
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra("conversationId", mConversations[pos].id)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemConversationListBinding.inflate(layoutInflater, parent, false)

        val v = binding.root
        v.setOnClickListener(mItemAction)

        return ConversationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mConversations.size
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(mConversations[position])
    }

    fun updateList(newList: ArrayList<Conversation>) {
        val oldList = ArrayList<Conversation>(mConversations)
        val diffResult = DiffUtil.calculateDiff(ConversationDiffCallback(oldList, newList), true)

        mConversations.clear()
        mConversations.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ConversationViewHolder(private val binding: ItemConversationListBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            binding.conversation = conversation
            binding.executePendingBindings()
        }
    }

    class ConversationDiffCallback(private val oldList : List<Conversation>,
                                   private val newList : List<Conversation>) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].createdTime == newList[newItemPosition].createdTime
        }

    }
}