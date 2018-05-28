package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface RoomUserDao {
    @Query("SELECT * FROM User WHERE id IN (:userIds)")
    fun getById(userIds: List<String>): Single<List<RoomUser>>

    @Insert(onConflict = REPLACE)
    fun insertAll(users: Array<RoomUser>)

    @Query("SELECT * FROM User ORDER BY name ASC")
    fun getAll(): Single<List<RoomUser>>
}