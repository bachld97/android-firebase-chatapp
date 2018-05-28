package com.example.cpu02351_local.firebasechatapp.localdatabase

import android.content.Context
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomUser
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.MyRoomDatabase
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomConversation
import com.example.cpu02351_local.firebasechatapp.localdatabase.roomdatabase.RoomMessage
import com.example.cpu02351_local.firebasechatapp.model.AbstractMessage
import com.example.cpu02351_local.firebasechatapp.model.Conversation
import com.example.cpu02351_local.firebasechatapp.model.Message
import com.example.cpu02351_local.firebasechatapp.model.User
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomLocalDatabase @Inject constructor(appContext: Context) : LocalDatabase {
    private val room = MyRoomDatabase.instance(appContext)

    private val userDao = room.RoomUserDao()
    private val conversationDao = room.RoomConversationDao()
    private val messageDao = room.RoomMessageDao()

    override fun loadConversationAll(): Single<List<Conversation>> {
        return conversationDao.getAll()
                .subscribeOn(Schedulers.io())
                .map { loadLastMessage(it) }
    }

    override fun loadMessages(conversationId: String): Single<List<AbstractMessage>> {
        return messageDao.getAllInConversation(conversationId)
                .subscribeOn(Schedulers.io())
                .map { it.toListMessage() }
    }

    override fun saveMessageAll(messages: List<AbstractMessage>, conversationId: String): Completable {
        return Completable.fromAction { messageDao.insertAll(messages.toListRoomMessage(conversationId).toTypedArray()) }
                .subscribeOn(Schedulers.io())
    }


    private fun loadLastMessage(list: List<RoomConversation>): List<Conversation> {
        return list.map { it.toConversation(loadMessage(it.lastMessId))}
    }

    private fun loadMessage(messageId: String): AbstractMessage? {
        return messageDao.getById(messageId).toMessage()
    }

    override fun saveConversationAll(conversations: List<Conversation>): Completable {
        return Completable.fromAction { conversationDao.insertAll(conversations.toListRoomConversation().toTypedArray()) }
                .andThen { conversations.filter { it.lastMessage != null}
                        .forEach { messageDao.insertAll(arrayOf(RoomMessage.from(it.lastMessage!!, it.id))) }
                }
                .subscribeOn(Schedulers.io())
    }

    override fun loadUserAll(): Single<List<User>> {
        return userDao.getAll()
                .subscribeOn(Schedulers.io())
                .map { it.toListUser() }
    }
    override fun loadUserByIds(ids: List<String>): Single<List<User>> {
        return userDao.getById(ids)
                .subscribeOn(Schedulers.io())
                .map { it.toListUser() }
    }

    override fun saveUserAll(users: List<User>): Completable {
        return Completable.fromAction { userDao.insertAll(users.toListRoomUser().toTypedArray()) }
                .subscribeOn(Schedulers.io())
    }
}


private fun List<RoomMessage>.toListMessage(): List<AbstractMessage> {
    return this.map { it.toMessage() }
}
private fun List<RoomUser>.toListUser(): List<User> {
    return this.map { it.toUser() }
}

private fun List<User>.toListRoomUser(): List<RoomUser> {
    return this.map { RoomUser.from(it) }
}
private fun List<Conversation>.toListRoomConversation(): List<RoomConversation> {
    return this.map { RoomConversation.from(it) }
}
private fun List<AbstractMessage>.toListRoomMessage(conversationId: String): List<RoomMessage> {
    return this.map {RoomMessage.from(it, conversationId)}
}