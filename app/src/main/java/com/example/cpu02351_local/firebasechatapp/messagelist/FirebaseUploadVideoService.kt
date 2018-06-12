package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.VideoMessage
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*

class FirebaseUploadVideoService : UploadVideoService() {

    companion object {
        fun newInstance(context: Context, info: VideoUploadInfo): Intent {
            val intent = Intent(context, FirebaseUploadVideoService::class.java)
            intent.putExtra(INFO, info)
            return intent
        }
    }

    private val mStorageReference = FirebaseHelper.getVideoMessageReference()

    override fun upload(byteArray: ByteArray, videoPath: String, info: VideoUploadInfo) {
        val messageId = info.messageId
        val thumbRef = mStorageReference.child("$messageId.jpg")
        val vidRef = mStorageReference.child("$messageId.mp4")

        vidRef.putFile(Uri.fromFile(File(videoPath)))
                .continueWithTask { task ->
                    if (!task.isSuccessful)
                        throw Objects.requireNonNull(task.exception)!!
                    vidRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        info.videoLink = task.result.toString()
                        uploadBinary(thumbRef, byteArray, info)
                    }
                }
    }

    private fun uploadBinary(ref: StorageReference, data: ByteArray, info: VideoUploadInfo) {
        ref.putBytes(data)
                .continueWithTask { task ->
                    if (!task.isSuccessful)
                        throw Objects.requireNonNull(task.exception)!!
                    ref.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        info.thumbnailLink = task.result.toString()
                        updateFirebaseDatabase(info)
                    }
                }
    }

    private fun updateFirebaseDatabase(info: VideoUploadInfo) {
        val mess = VideoMessage(info.messageId, System.currentTimeMillis(), info.senderId, info.videoLink)
        mess.setMetadata(info.videoWidth, info.videoHeight)
        mess.thumbnailLink = info.thumbnailLink

        val firebaseMessLoader = FirebaseMessageLoader()
        firebaseMessLoader.uploadVideoMessage(mess, info.conversationId, info.byUsers)
    }

}