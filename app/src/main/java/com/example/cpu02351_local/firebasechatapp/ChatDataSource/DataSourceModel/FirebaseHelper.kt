package com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FirebaseHelper {
    companion object {
        private const val STORAGE_BASE_URL = "gs://fir-chat-47b52.appspot.com"
        @JvmStatic
        fun getFirebaseInstance() = FirebaseDatabase.getInstance()!!

        @JvmStatic
        private fun getStorageReference() =  FirebaseStorage.getInstance()

        @JvmStatic
        fun getAvatarReference(userId: String) =
            getStorageReference().getReferenceFromUrl("$STORAGE_BASE_URL/$userId")

    }
}