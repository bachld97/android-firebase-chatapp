package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(RoomUser::class)], version = 1)
abstract class RoomUserDatabase : RoomDatabase() {
    abstract fun RoomUserDao(): RoomUserDao

    companion object {
        private var INSTANCE: RoomUserDatabase? = null
        fun instance(appContext: Context): RoomUserDatabase {
            if (INSTANCE == null) {
                synchronized(RoomUserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(appContext,
                            RoomUserDatabase::class.java, "user.db")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}

