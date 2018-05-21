package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import android.net.Uri
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.AVA_URL
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
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
            val uploadTask = ref.putFile(filePath)
            uploadTask.continueWith(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    // Failed up upload data?
                    emitter.onError(task.exception!!)
                    throw task.exception!!
                } else {
                    return@Continuation ref.downloadUrl
                }
            }).addOnSuccessListener { task ->
                if (task.isSuccessful) {
                    updateAvatarUrl(userId, task.result.toString())
                }
                emitter.onComplete()
            }.addOnCanceledListener {
                emitter.onError(Throwable())
            }
        }
    }

    override fun updateAvatarUrl(userId: String, avaUrl: String) {
        databaseRef.child("$USERS/$userId/$AVA_URL")
                .setValue(avaUrl)
    }
}