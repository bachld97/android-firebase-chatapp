package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.Message
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.DELIM

@Entity(tableName = "Conversation")
data class RoomConversation(@PrimaryKey var id: String,
                       var lastModified: Long,
                       var byUser: String,
                       var lastMessId: String = "") {
    constructor(): this("", -1, "", "")

    @Ignore
    fun toConversation(lastMess: Message?): Conversation {
        val con = Conversation(id)
        con.participantIds = byUser.split(Conversation.ID_DELIM)
        con.createdTime = lastModified
        con.lastMessage = lastMess
        return con
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(conversation: Conversation): RoomConversation {
            return RoomConversation(conversation.id, conversation.createdTime,
                    conversation.participantIds.joinToString(Conversation.ID_DELIM),
                    conversation.lastMessage?.id ?: "")
        }
    }
}