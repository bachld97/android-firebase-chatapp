package com.example.cpu02351_local.firebasechatapp.localdatabase

import android.content.Context
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomUser
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomUserDatabase
import com.example.cpu02351_local.firebasechatapp.model.User
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomLocalUserDatabase @Inject constructor(appContext: Context) : LocalUserDatabase {


    private val userDatabase = RoomUserDatabase.instance(appContext)

    override fun loadByIds(ids: List<String>): Single<List<User>> {
        Log.d("DEBUGGING", "Ids: ${ids[0]}")
        return userDatabase.RoomUserDao().getById(ids)
                .subscribeOn(Schedulers.io())
                .map { it.toListUser() }
        /*
        return Single
                .just(userDatabase.RoomUserDao().getById(ids).toListUser())
                .subscribeOn(Schedulers.io())
        */
    }

    override fun saveAll(users: List<User>): Completable {
        return Completable.fromAction { userDatabase.RoomUserDao().insertAll(users.toListRoomUser().toTypedArray()) }
                .subscribeOn(Schedulers.io())
    }

}

private fun List<RoomUser>.toListUser(): List<User> {
    return this.map { it.toUser() }
}

private fun List<User>.toListRoomUser(): List<RoomUser> {
    return this.map { RoomUser.from(it) }
}
