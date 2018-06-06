package com.example.cpu02351_local.firebasechatapp.utils

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.cpu02351_local.firebasechatapp.R
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GlideDataBinder {

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String?) {
            val context = imageView.context
            val options = RequestOptions()
            options.placeholder(R.drawable.ic_default_ava)
            options.diskCacheStrategy(DiskCacheStrategy.NONE)
            options.skipMemoryCache(true)
            Glide.with(context).load(url)
                    .apply(options)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("smallImageUrl")
        fun setMessageImageUrl(imageView: ImageView, url: String) {
            val context = imageView.context
            val options = RequestOptions()
            options.placeholder(R.drawable.ic_default_ava)
            options.sizeMultiplier(0.5f)
            Glide.with(context).load(url)
                    .apply(options)
                    .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("imageWithSize")
        fun setImageWithSize(imageView: ImageView, bundle: Bundle) {
            val w = bundle.getInt("width")
            val h = bundle.getInt("height")
            val params = imageView.layoutParams
            params.width = w
            params.height = h
            imageView.layoutParams = params

            if (bundle.getBoolean("hasBitmap")) {
                /*
                val bytes = bundle.getByteArray("bitmap")
                Single.create<Bitmap> {
                    it.onSuccess(BitmapFactory.decodeByteArray(bytes, 0, bytes.size)) }
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { bitmap, _ -> imageView.setImageBitmap(bitmap) }
                        */
            }
            else {
                val url = bundle.getString("url")
                Glide.with(imageView)
                        .load(url)
                        .into(imageView)
            }
        }

        fun setImageOffline(imageView: ImageView, pictureUri: Uri) {
            val context = imageView.context
            val options = RequestOptions()
            options.placeholder(R.drawable.ic_default_ava)
            options.centerCrop()
            options.sizeMultiplier(0.3f)
            Glide.with(context).load("file://${pictureUri.path}")
                    .apply(options)
                    .into(imageView)
        }
    }
}
