package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.example.cpu02351_local.firebasechatapp.model.Message

@Dao
interface RoomMessageDao {
    @Query("SELECT * FROM  Message WHERE id = (:id)")
    fun getById(id: String): Message

    @Insert(onConflict = REPLACE)
    fun insertAll(messages: Array<RoomMessage>)
}