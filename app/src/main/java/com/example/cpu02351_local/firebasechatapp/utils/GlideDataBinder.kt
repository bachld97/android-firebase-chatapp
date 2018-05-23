package com.example.cpu02351_local.firebasechatapp.utils

import android.databinding.BindingAdapter
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


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

        @JvmStatic
        @BindingAdapter("smallImageUrl")
        fun setMessageImageUrl(imageView: ImageView, url: String) {
            val context = imageView.context
            val options = RequestOptions()
            options.centerCrop()
            options.sizeMultiplier(0.2f)
            Glide.with(context).load(url)
                    .thumbnail(0.15f)
                    .apply(options)
                    .into(imageView)
        }

        fun setImageOffline(imageView: ImageView, pictureUri: Uri) {
            val context = imageView.context
            val options = RequestOptions()
            options.centerCrop()
            options.sizeMultiplier(0.3f)
            Glide.with(context).load("file://${pictureUri.path}")
                    .apply(options)
                    .into(imageView)
            Log.d("DEBUGGING", pictureUri.path)
        }
    }
}
