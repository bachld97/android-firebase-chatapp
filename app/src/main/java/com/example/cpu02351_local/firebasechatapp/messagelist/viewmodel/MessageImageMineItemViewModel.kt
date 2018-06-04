package com.example.cpu02351_local.firebasechatapp.messagelist.viewmodel

import android.graphics.*
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.messagelist.model.MessageItem
import com.example.cpu02351_local.firebasechatapp.model.messagetypes.ImageMessage
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.cpu02351_local.firebasechatapp.messagelist.MessageItemAdapter


class MessageImageMineItemViewModel(messageItem: MessageItem, var v: View, private val imageClick: MessageItemAdapter.ItemClickCallback) : MessageBaseItemViewModel(messageItem) {
    fun hasBitmap(): Boolean {
        val imgMessage = messageItem.message as ImageMessage
        val imv = v.findViewById<ImageView>(R.id.bitmap_display)
        if (!imgMessage.hasBitmap) {
            Glide.with(v)
                    .asBitmap()
                    .load(imgMessage.content)
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            // Do nothing
                            return false
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (resource != null) {
                                imgMessage.onBitmapLoaded(resource)
                            }
                            return false
                        }
                    })
                    .into(imv)
        }
        return imgMessage.hasBitmap
    }
    fun getImageSrc(): Drawable {
        val imgMessage = messageItem.message as ImageMessage
        return BitmapDrawable(v.context.resources,
                roundedBitmap(imgMessage.cachedBitmap!!))
    }

    fun imageClicked() {
        imageClick.onClick(messageItem)
    }

    // This method should be moved to viewholder / messageitem
    private fun roundedBitmap(bitmap: Bitmap): Bitmap {
        return bitmap /*
        val output = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = 16f

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
        */
    }
}
