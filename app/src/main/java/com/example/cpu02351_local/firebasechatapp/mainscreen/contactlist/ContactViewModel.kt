package com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist

import android.content.Context
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.localdatabase.LocalDatabase
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.User
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageListActivity
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*

class ContactViewModel(private val contactLoader: ContactLoader,
                       private val contactView: ContactView,
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
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }


    fun dispose() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
    }

    private fun loadContacts() {
        val obs = contactLoader.loadContacts(userId)
        dispose()
        obs.subscribe(object : SingleObserver<List<User>> {
            override fun onSuccess(t: List<User>) {
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

    fun onContactClicked(context: Context, user: User) {
        val userIds = arrayOf(userId, user.id)
        val conId = Conversation.uniqueId(userIds)
        val intent = MessageListActivity.newInstance(context, conId, conId)
        context.startActivity(intent)
    }

    fun onSelectedContactsAction(contacts: List<ContactItem>) {
        val userIdString = contacts.joinToString(Conversation.ID_DELIM) { it.contactId }
                .plus("${Conversation.ID_DELIM}$userId")
        val userIds = userIdString.split(Conversation.ID_DELIM).toTypedArray()
        val conId = Conversation.uniqueId(userIds)
        contactView.navigate(conId, userIdString)
    }

    private var selectedMessageItem = Collections.emptyList<ContactItem>()
    fun createGroupChat() {
        onSelectedContactsAction(selectedMessageItem)
    }

    fun onSelectedContactChanged(selectedPosition: List<Int>, selectedMessageItem: List<ContactItem>) {
        this.selectedMessageItem = selectedMessageItem
        contactView.onContactItemSelected(selectedPosition)
    }
}