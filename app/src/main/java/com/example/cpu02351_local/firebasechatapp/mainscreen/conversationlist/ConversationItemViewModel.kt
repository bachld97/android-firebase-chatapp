package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

import android.os.Build
import android.text.Html
import android.text.Spanned

class ConversationItemViewModel(var item: ConversationItem) {
   // fun getLastMessagePreview(): String {
   //     return item.lastMessagePreview
   // }

    fun getLastMessagePreview(): Spanned {
        val text = item.lastMessagePreview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(text)
        }
    }



    fun getLastSenderName(): String = item .lastSenderName + ":"

    fun getElapseTimeDisplay(): String = item.elapseTimeDisplay

    fun getConversationDisplayName(): String = item.conversationDisplayName

    fun getAvaDisplayUrl(): String = item.displayAvaUrl

    fun getShouldDisplaySender(): Boolean = item.shouldDisplaySender

    fun getIsRead(): Boolean = item.isRead
    fun getAvaDisplayUrlFirst(): String = item.displayAvaUrl1
    fun getAvaDisplayUrlSecond(): String = item.displayAvaUrl2
    fun getAvaDisplayUrlThird(): String = item.displayAvaUrl3
    fun getAvaDisplayUrlFourth(): String = item.displayAvaUrl4
}
