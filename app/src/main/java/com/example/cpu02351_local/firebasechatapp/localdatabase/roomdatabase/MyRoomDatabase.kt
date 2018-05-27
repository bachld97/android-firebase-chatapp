package com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [RoomUser::class, RoomMessage::class, RoomConversation::class], version = 2)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun RoomUserDao(): RoomUserDao
    abstract fun RoomConversationDao(): RoomConversationDao
    abstract fun RoomMessageDao(): RoomMessageDao

    companion object {
        private var INSTANCE: MyRoomDatabase? = null
        fun instance(appContext: Context): MyRoomDatabase {
            if (INSTANCE == null) {
                synchronized(MyRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(appContext,
                            MyRoomDatabase::class.java, "chat.db")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}

