package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.TextMessage


@Entity(tableName = "Message")
data class RoomMessage(@PrimaryKey var id: String,
                       var conversationId: String,
                       var byUser: String,
                       var atTime: Long,
                       var content: String,
                       var isSending: Boolean) {
    constructor() : this("","", "",-1,"", false)

    @Ignore
    fun toMessage(): AbstractMessage {
        val mess = TextMessage(id)
        mess.byUser = byUser
        mess.atTime = atTime
        mess.content = content
        mess.isSending = isSending
        return mess
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(mess: AbstractMessage, conversationId: String): RoomMessage {
            return RoomMessage(mess.id, conversationId, mess.byUser, mess.atTime, mess.content, mess.isSending)
        }
    }
}