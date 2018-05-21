package com.example.cpu02351_local.firebasechatapp.Utils

import android.databinding.BindingAdapter
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.net.URL


class GlideDataBinder {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String) {
            val context = imageView.context
            val options = RequestOptions()
            options.centerCrop()
            options.diskCacheStrategy(DiskCacheStrategy.NONE)
            options.skipMemoryCache(true)
            options.sizeMultiplier(0.3f)
            Glide.with(context).load(url)
                    .thumbnail(0.25f)
                    .apply(options)
                    .into(imageView)
        }

        fun setImageOffline(imageView: ImageView, url: Uri) {
            val context = imageView.context
            val options = RequestOptions()
            options.centerCrop()
            options.sizeMultiplier(0.3f)
            Glide.with(context).load(File(url.path))
                    .thumbnail(0.25f)
                    .apply(options)
                    .into(imageView)
            Log.d("DEBUGGING", url.path)
        }
    }
}
