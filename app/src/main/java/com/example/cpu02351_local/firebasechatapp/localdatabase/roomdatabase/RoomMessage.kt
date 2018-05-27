package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.Message


@Entity(tableName = "Message")
data class RoomMessage(@PrimaryKey var id: String,
                       var conversationId: String,
                       var byUser: String,
                       var atTime: Long,
                       var content: String) {
    constructor() : this("","", "",-1,"")

    @Ignore
    fun toMessage(): Message {
        val con = Message(id)
        con.byUser = byUser
        con.atTime = atTime
        con.content = content
        return con
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(mess: Message, conversationId: String): RoomMessage {
            return RoomMessage(mess.id, conversationId, mess.byUser ?: "", mess.atTime, mess.content)
        }
    }
}