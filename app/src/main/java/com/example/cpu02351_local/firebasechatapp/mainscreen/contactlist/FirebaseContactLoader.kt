package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DaggerFirebaseReferenceComponent
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.DataSourceModel.FirebaseUser
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseHelper.Companion.USERS
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FirebaseContactLoader : ContactLoader {

    @Inject
    lateinit var databaseRef: DatabaseReference

    init {
        DaggerFirebaseReferenceComponent.create().injectInto(this)
    }


    override fun loadContacts(userId: String): Observable<List<User>> {
        val reference = databaseRef.child(USERS)
        lateinit var listener: ValueEventListener
        val obs = Observable.create<List<User>> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val resArray = ArrayList<User>()
                    snapshot?.children?.filter { it.key != userId }
                            ?.forEach {
                                val contactId = it.key
                                loadContact(contactId).subscribe(object : SingleObserver<User> {
                                    private lateinit var disposable: Disposable
                                    override fun onSuccess(t: User) {
                                        resArray.add(t)
                                        emitter.onNext(resArray)
                                        if (!disposable.isDisposed) disposable.dispose()
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                        disposable = d
                                    }

                                    override fun onError(e: Throwable) {
                                        emitter.onError(Throwable("Error loading contact with id: $contactId"))
                                    }
                                })
                            }
                }

                override fun onCancelled(p0: DatabaseError?) {
                    // Do nothing for now
                }
            }
            reference.addValueEventListener(listener)
        }
        return obs.doFinally { reference.removeEventListener(listener) }
    }

    override fun loadContact(contactId: String): Single<User> {
        val reference = databaseRef.child("$USERS/$contactId")
        lateinit var listener: ValueEventListener
        val obs = Single.create<User> { emitter ->
            listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot?) {
                    val con = FirebaseUser()
                    con.fromMap(contactId, snapshot?.value)
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
}