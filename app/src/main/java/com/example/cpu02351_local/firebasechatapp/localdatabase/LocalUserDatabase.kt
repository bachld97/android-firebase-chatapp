package com.example.cpu02351_local.firebasechatapp.localdatabase

import com.example.cpu02351_local.firebasechatapp.model.User
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Single

interface LocalUserDatabase {
    fun loadByIds(ids : List<String>): Single<List<User>>
    fun loadAll(): Single<List<User>>
    fun saveAll(users: List<User>): Completable
}