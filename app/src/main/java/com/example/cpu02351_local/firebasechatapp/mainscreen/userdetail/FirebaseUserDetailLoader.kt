package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import android.net.Uri
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.AVA_URL
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.DaggerFirebaseReferenceComponent
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class FirebaseUserDetailLoader : UserDetailLoader {

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }

    override fun loadUserDetail(userId: String): Single<User> {
        val reference = databaseRef.child("$USERS/$userId")
        lateinit var listener: ValueEventListener
        val obs = Single.create<User> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val con = FirebaseUser()
                    con.fromMap(userId, snapshot?.value)
                    emitter.onSuccess(con.toUser())
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("Cannot fetch user information"))
                }
            }
            reference.addListenerForSingleValueEvent(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener) }
    }

    override fun changeAvatar(userId: String, filePath: Uri): Completable {
        return Completable.create { emitter ->
            val ref = FirebaseHelper.getAvatarReference(userId)
            ref.putFile(filePath)
            ref.downloadUrl.addOnSuccessListener {
                updateAvatarUrl(userId, it.toString())
                emitter.onComplete()
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    override fun updateAvatarUrl(userId: String, avaUrl: String) {
        databaseRef.child("$USERS/$userId/$AVA_URL")
                .setValue(avaUrl)
    }
}