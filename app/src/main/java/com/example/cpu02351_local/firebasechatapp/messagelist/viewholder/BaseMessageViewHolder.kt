package com.example.cpu02351_local.firebasechatapp.messagelist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage

abstract class BaseMessageViewHolder(v: View): RecyclerView.ViewHolder(v) {
     abstract fun bind(message: AbstractMessage, showAva: Boolean, showTime: Boolean, avaUrl: String, displayName: String)
}