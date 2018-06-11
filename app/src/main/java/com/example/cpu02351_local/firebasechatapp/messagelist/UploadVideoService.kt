package com.example.cpu02351_local.firebasechatapp.messagelist

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.cpu02351_local.firebasechatapp.utils.FirebaseHelper
import com.iceteck.silicompressorr.SiliCompressor
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class UploadVideoService : IntentService("UploadVideoService") {

    companion object {
        const val INFO = "upload_info"
        fun newInstance(context: Context, info: VideoUploadInfo): Intent {
            val intent = Intent(context, UploadVideoService::class.java)
            intent.putExtra(INFO, info)
            return intent
        }
    }

    private val handlerForCompressedVideo = object : CompletableObserver {
        override fun onComplete() {
            // startUploadingTask()
        }

        override fun onSubscribe(d: Disposable) {
            disposable.add(d)
        }

        override fun onError(e: Throwable) {
            // Do nothing, service ends itself
        }

    }


    private lateinit var mVideoPath: String
    private val disposable = mutableListOf<Disposable>()
    override fun onDestroy() {
        disposable.forEach {
            if (!it.isDisposed)
                it.dispose()
        }
        super.onDestroy()
    }

    override fun onHandleIntent(intent: Intent?) {
        val info = intent?.getSerializableExtra(INFO) as VideoUploadInfo
        mMessageId = "test"
        val videoPath = info.filePath
        mVideoPath = videoPath
        val storagePath = Environment.getExternalStorageDirectory().path + "/AwesomeChat/Video/"
        Completable.create { emitter ->
            mVideoPath = SiliCompressor.with(applicationContext).compressVideo(videoPath, storagePath)
            emitter.onComplete()}
                .subscribeOn(Schedulers.io())
                // .subscribe()
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        startUploadingTask()
                    }

                    override fun onSubscribe(d: Disposable) {
                        // disposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                        // Do nothing, service ends itself
                    }

                })
    }

    private lateinit var mMessageId: String
    private val mStorageReference = FirebaseHelper.getVideoMessageReference()
    private fun startUploadingTask() {
        // val bitmap = generateThumbnail()
        Log.d("DEBUG_PROGRESS", "Uploading")

        val thumbRef = mStorageReference.child("$mMessageId.jpg")
        val vidRef = mStorageReference.child("$mMessageId.mp4")


        vidRef.putFile(Uri.fromFile(File(mVideoPath)))
                .continueWithTask { task ->
                    if (!task.isSuccessful)
                        throw Objects.requireNonNull(task.exception)!!
                    vidRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        Log.d("DEBUG_PROGRESS", "Uploaded: $downloadUri")
                    }
                }
    }

    private fun generateThumbnail(): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(mVideoPath)
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release()
            }
        }
        return bitmap
    }

}