package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.Conversation

@Entity(tableName = "Conversation")
data class RoomConversation(@PrimaryKey var id: String,
                            var lastModified: Long,
                            var byUser: String,
                            var lastMessId: String = "",
                            var isRead: Boolean = false) {
    constructor(): this("", -1, "", "")

    @Ignore
    fun toConversation(lastMess: AbstractMessage?): Conversation {
        val con = Conversation(id)
        con.participantIds = byUser.split(Conversation.ID_DELIM)
        con.createdTime = lastModified
        con.lastMessage = lastMess
        con.isRead = isRead
        return con
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(conversation: Conversation): RoomConversation {
            return RoomConversation(conversation.id, conversation.createdTime,
                    conversation.participantIds.joinToString(Conversation.ID_DELIM),
                    conversation.lastMessage?.id ?: "",
                    conversation.isRead)
        }
    }
}