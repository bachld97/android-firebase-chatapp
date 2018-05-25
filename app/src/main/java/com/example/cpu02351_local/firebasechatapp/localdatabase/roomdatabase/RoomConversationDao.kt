package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface RoomConversationDao {
    @Query("SELECT * FROM Conversation")
    fun getAll(): Single<List<RoomConversation>>

    @Insert(onConflict = REPLACE)
    fun insertAll(conversation: Array<RoomConversation>)
}