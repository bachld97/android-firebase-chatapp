package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import com.example.cpu02351_local.firebasechatapp.model.User

interface UserDetailView {
    fun onUserDetailLoaded(userDetail: User)
    fun onUpdateAvatarFailed()

}