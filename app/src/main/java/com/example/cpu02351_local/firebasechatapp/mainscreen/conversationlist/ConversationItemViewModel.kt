package com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist

class ConversationItemViewModel(var item: ConversationItem) {
    fun getLastMessagePreview(): String {
        return item.lastMessagePreview
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
