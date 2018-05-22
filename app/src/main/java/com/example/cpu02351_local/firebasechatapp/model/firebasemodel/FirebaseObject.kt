package com.example.cpu02351_local.firebasechatapp.model.firebasemodel

abstract class FirebaseObject {
    abstract fun toMap() : Map<String, Any>
    abstract fun fromMap(id: String, value: Any?)
}