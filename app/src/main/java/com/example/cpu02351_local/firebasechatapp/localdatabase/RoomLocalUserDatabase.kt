package com.example.cpu02351_local.firebasechatapp.localdatabase

import android.content.Context
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomUserDatabase
import com.example.cpu02351_local.firebasechatapp.model.User
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomLocalUserDatabase @Inject constructor(appContext: Context) : LocalUserDatabase {


    val userDatabase = RoomUserDatabase.instance(appContext)

    override fun loadByIds(ids: List<String>): Single<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAll(users: List<User>): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}