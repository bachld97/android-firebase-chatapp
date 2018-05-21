package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import android.net.Uri
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface UserDetailLoader {
    fun loadUserDetail(userId: String): Single<User>
    fun changeAvatar(userId: String, filePath: Uri): Completable
    fun updateAvatarUrl(userId: String, avaUrl: String)
}