package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.model.firebasemodel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.utils.DaggerFirebaseReferenceComponent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import javax.inject.Inject

class FirebaseContactLoader : ContactLoader {

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }


    override fun loadContacts(userId: String): Single<List<User>> {
        val reference = databaseRef.child(USERS)
        lateinit var listener: ValueEventListener
        val obs = Single.create<List<User>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val res = snapshot?.children
                            ?.map { it -> FirebaseUser().toUserFromMap(it.key, it.value) }
                            ?.filter { it.id != userId }
                    if (res != null) {
                        emitter.onSuccess(res)
                    } else {
                        emitter.onError(Throwable("No contact found"))
                    }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    emitter.onError(Throwable("No contact found"))
                }
            }
            reference.addValueEventListener(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener) }
    }
}