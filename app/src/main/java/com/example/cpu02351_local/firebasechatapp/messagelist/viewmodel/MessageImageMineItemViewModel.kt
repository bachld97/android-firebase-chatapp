package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter
import com.example.cpu02351_local.firebasechatapp.messagelist.model.ImageMessageItem
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream


class MessageImageMineItemViewModel(messageItem: MessageItem, var v: View, private val imageClick: MessageItemAdapter.ItemClickCallback,
                                    private val imageSendRetry: MessageItemAdapter.ItemClickCallback) : MessageBaseItemViewModel(messageItem) {



    fun imageClicked() {
        imageClick.onClick(messageItem)
    }

    fun retrySend() {
        imageSendRetry.onClick(messageItem)
    }

    fun getAttributeBundle(): Bundle {
        val res = Bundle()
        res.putInt("width", (messageItem as ImageMessageItem).width)
        res.putInt("height", (messageItem as ImageMessageItem).height)
        val b = (messageItem.message as ImageMessage).bitmap
        res.putBoolean("hasBitmap", b != null)
        /*
        if (b != null) {
            Single.create<ByteArray> {
                val stream = ByteArrayOutputStream()
                b.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                it.onSuccess(stream.toByteArray()) }
                    .subscribeOn(Schedulers.computation())
                    .subscribe { byteArray, _ ->
                        res.putByteArray("bitmap", byteArray)
                    }
        } */
        val mess = messageItem.message
        val url = if (mess is ImageMessage) {
            if (mess.localUri.toString().isNotEmpty()) {
                mess.localUri.toString()
            } else {
                messageItem.getContent()
            }
        } else {
            messageItem.getContent()
        }

        res.putString("url", url)
        return res
    }
}
