package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

abstract class FirebaseObject {
    abstract fun toMap() : Map<String, Any>
}