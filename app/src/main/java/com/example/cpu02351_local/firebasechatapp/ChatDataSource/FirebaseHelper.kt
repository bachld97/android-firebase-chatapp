package com.example.cpu02351_local.firebasechatapp.ChatDataSource

import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.FirebaseContactLoader
import com.example.cpu02351_local.firebasechatapp.mainscreen.conversationlist.FirebaseConversationLoader
import com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail.FirebaseUserDetailLoader
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Component
import dagger.Module
import dagger.Provides

@Module
class FirebaseHelper {
    companion object {
        const val AVA_URL = "ava_url"
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"
        const val MESSAGE = "messages"
        const val CONTACTS = "contacts"
        const val BY_USER = "by_user"
        const val BY_USERS = "by_users"
        const val LAST_MOD = "last_mod"
        const val TIME = "at_time"
        const val DELIM = " "
        const val USERNAME = "name"
        const val TYPE = "type"
        const val CONTENT = "content"
        const val PASSWORD = "password"
        const val AVA = "ava"

        private const val STORAGE_BASE_URL = "gs://fir-chat-47b52.appspot.com"
        @JvmStatic
        fun getFirebaseInstance() = FirebaseDatabase.getInstance()!!

        @JvmStatic
        private fun getStorageReference() =  FirebaseStorage.getInstance()

        @JvmStatic
        fun getAvatarReference(userId: String) =
            getStorageReference().getReferenceFromUrl("$STORAGE_BASE_URL/$AVA/$userId")

    }
    @Provides fun getFirebaseReference(): DatabaseReference = getFirebaseInstance().reference!!
}

@Component(modules = [(FirebaseHelper::class)])
interface FirebaseReferenceComponent {
    fun injectInto(loader: FirebaseContactLoader)
    fun injectInto(loader: FirebaseConversationLoader)
    fun injectInto(loader: FirebaseUserDetailLoader)
}