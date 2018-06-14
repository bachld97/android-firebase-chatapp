package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem

class TextMessageOtherItemViewModel(messageItem: MessageItem,
                                    private val clickCallback: MessageItemAdapter.ItemClickCallback,
                                    private val longClickCallback: MessageItemAdapter.ItemLongClickCallback) : MessageOtherItemViewModel(messageItem) {
    //fun getMessageContent(): String = messageItem.getContent()
    fun getMessageContent(): Spanned {
        val content = messageItem.getContent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(content)
        }
    }

    fun messageLongClicked(): Boolean {
        longClickCallback.onLongClick(messageItem)
        return true
    }

    fun messageClicked() {
        clickCallback.onClick(messageItem)
    }
}
