package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import android.net.Uri
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.model.User
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class UserDetailViewModel(private val userDetailLoader: UserDetailLoader,
                           private val userDetailView: UserDetailView,
                           private val userId: String)  {
    private var mUserDetailDisposable: Disposable? = null
    private var mUpdateAvatarDisposable: Disposable? = null

    init {
        loadUserDetail()
    }

    fun dispose() {
        if (mUserDetailDisposable != null && !mUserDetailDisposable!!.isDisposed) {
            mUserDetailDisposable!!.dispose()
        }
        if (mUpdateAvatarDisposable != null && !mUpdateAvatarDisposable!!.isDisposed) {
            mUpdateAvatarDisposable!!.dispose()
        }
    }

    private fun loadUserDetail() {
        Log.d("DEBUGGING", "Run man :)")
        val obs = userDetailLoader.loadUserDetail(userId)
        dispose()
        obs.subscribe(object : SingleObserver<User> {
            override fun onSuccess(t: User) {
                userDetailView.onUserDetailLoaded(t)
                if (mUserDetailDisposable != null && !mUserDetailDisposable!!.isDisposed) {
                    mUserDetailDisposable!!.dispose()
                }
            }

            override fun onSubscribe(d: Disposable) {
                mUserDetailDisposable = d
            }

            override fun onError(e: Throwable) {
                // Do nothing for now
            }
        })
    }

    fun changeAvatar(filePath: Uri) {
        val completable =  userDetailLoader.changeAvatar(userId, filePath)
        completable.subscribe(object : CompletableObserver {
            override fun onComplete() {
                if (mUpdateAvatarDisposable != null && !mUpdateAvatarDisposable!!.isDisposed) {
                    mUpdateAvatarDisposable!!.dispose()
                }
                loadUserDetail()
            }

            override fun onSubscribe(d: Disposable) {
                mUpdateAvatarDisposable = d
            }

            override fun onError(e: Throwable) {
                userDetailView.onUpdateAvatarFailed()
            }
        })
    }
}