package com.example.cpu02351_local.firebasechatapp.messagelist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
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
    override fun upload(byteArray: ByteArray, videoPath: String, messageId: String) {
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
                        val downloadUri = task.result
                        Log.d("DEBUG_PROGRESS", "Uploaded: $downloadUri")
                        uploadBinary(thumbRef, byteArray)
                    }
                }
    }

    private fun uploadBinary(ref: StorageReference, data: ByteArray) {
        ref.putBytes(data)
                .continueWithTask { task ->
                    if (!task.isSuccessful)
                        throw Objects.requireNonNull(task.exception)!!
                    ref.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("DEBUG_PROGRESS", "Thumbnail uploaded: $downloadUri")
                    }
                }
    }
}