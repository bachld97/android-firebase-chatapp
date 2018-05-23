package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "User")
data class RoomUser(
        @PrimaryKey var id: String,
        var name: String,
        var conversations: String,
        var avaUrl: String) {
    constructor(): this("", "", "", "")

}
