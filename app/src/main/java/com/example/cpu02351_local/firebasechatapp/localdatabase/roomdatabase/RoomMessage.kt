package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.TextMessage
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.VideoMessage


@Entity(tableName = "Message")
data class RoomMessage(@PrimaryKey var id: String,
                       var conversationId: String,
                       var byUser: String,
                       var atTime: Long,
                       var content: String,
                       var isSending: Boolean,
                       var type: String,
                       var additionalContent: String) {
    constructor() : this("","", "",-1,"", false, "", "")


    @Ignore
    var isFailed: Boolean = false

    @Ignore
    fun toMessage(): AbstractMessage {

        val mess = when (type) {
            "text" -> {
                TextMessage()
            }
            "image" -> {
                val dimen = additionalContent.split("@")
                val w = dimen[0].toInt()
                val h = dimen[1].toInt()

                if (isSending) {
                    isSending = false
                    isFailed = true
                }
                ImageMessage(w, h)
            }
            "video" -> {
                val additionalInfos = additionalContent.split("@")
                val w = additionalInfos[0].toInt()
                val h = additionalInfos[1].toInt()
                val thumb = additionalInfos[2]

                if (isSending) {
                    isSending = false
                    isFailed = true
                }

                VideoMessage(w, h, thumb)
            }
            else -> throw RuntimeException("Unexpected message type")
        }
        mess.id = id
        mess.byUser = byUser
        mess.atTime = atTime
        mess.isSending = isSending
        mess.isFailed = isFailed
        mess.content = content
        return mess
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(mess: AbstractMessage, conversationId: String): RoomMessage {

            val id = mess.id
            val byUser = mess.byUser
            val atTime = mess.atTime
            val isSending = mess.isSending
            var content = mess.content

            return when (mess) {
                is TextMessage -> {
                    RoomMessage(id, conversationId, byUser, atTime, content, isSending, "text", "")
                }
                is ImageMessage -> {
                    if (mess.localUri.toString().isNotEmpty()) {
                        content = mess.localUri.toString()
                    }
                    return RoomMessage(id, conversationId, byUser, atTime, content, isSending
                            , "image", "${mess.width}@${mess.height}")
                }
                is VideoMessage -> {
                    val additionalContent = "${mess.width}@${mess.height}@${mess.thumbnailLink}"
                    return RoomMessage(
                            id, conversationId, byUser, atTime, content,
                            isSending, "video", additionalContent)
                }
                else -> throw RuntimeException("Unexpected message type")
            }
        }
    }
}