package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.example.cpu02351_local.firebasechatapp.model.User

@Entity(tableName = "User")
data class RoomUser(
        @PrimaryKey var id: String,
        var name: String,
        var conversations: String,
        var avaUrl: String) {
    constructor(): this("", "", "", "")


    @Ignore
    fun toUser(): User {
        return User(id, name, conversations, avaUrl)
    }

    companion object {
        @JvmStatic
        @Ignore
        fun from(user: User): RoomUser {
            return RoomUser(user.id, user.name, user.conversations, user.avaUrl)
        }
    }
}
