package com.example.cpu02351_local.firebasechatapp.utils

import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactItem
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactLoader
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactView
import com.example.cpu02351_local.firebasechatapp.model.User
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

open class ContactProducerViewModel(private val contactLoader: ContactLoader,
                                    private val contactView: ContactConsumerView,
                                    private val userId: String) {

    private var mDisposable: Disposable? = null
    private var localDatabase: LocalDatabase? = null

    init {
        loadContacts()
    }

    fun setLocalUserDatabase(localDatabase: LocalDatabase) {
        this.localDatabase = localDatabase
        loadLocalContacts()
    }

    private var mLocalDisposable: Disposable ?= null
    private fun loadLocalContacts() {
        localDatabase?.loadUserAll()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : SingleObserver<List<User>> {
                    override fun onSuccess(t: List<User>) {
                        notifyLocalData(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                        mLocalDisposable = d
                    }

                    override fun onError(e: Throwable) {
                        // Do nothing
                    }
                })
    }

    fun dispose() {
        disposeLocal()
        disposeNetwork()
    }

    private fun disposeLocal() {
        if (mLocalDisposable != null && !mLocalDisposable!!.isDisposed) {
            mLocalDisposable!!.dispose()
        }
    }

    private fun disposeNetwork() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadContacts() {
        val obs = contactLoader.loadContacts(userId)
        disposeNetwork()
        obs.subscribe(object : Observer<List<User>> {
            override fun onComplete() {
                // Do nothing?
            }

            override fun onNext(t: List<User>) {
                disposeLocal()
                localDatabase?.saveUserAll(t)
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe {
                            Log.d("DEBUGGING", "save finish")
                        }
                notifyNetworkData(t)
            }

            override fun onSubscribe(d: Disposable) {
                mDisposable = d
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }

        })
    }

    private fun notifyNetworkData(data: List<User>) {
        contactView.onNetworkContactsLoaded(data.filter { it.id != userId}
                .map { ContactItem(it) }
                .sortedBy { it.contactName.toUpperCase() })
    }

    private fun notifyLocalData(data: List<User>) {
        contactView.onLocalContactsLoaded(data.filter { it.id != userId}
                .map { ContactItem(it) }
                .sortedBy { it.contactName.toUpperCase() })
    }
}