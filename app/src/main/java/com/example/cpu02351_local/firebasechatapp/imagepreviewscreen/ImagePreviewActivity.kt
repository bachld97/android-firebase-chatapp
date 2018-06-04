package com.example.cpu02351_local.firebasechatapp.imagepreviewscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cpu02351_local.firebasechatapp.R

class ImagePreviewActivity : AppCompatActivity() {

    companion object {
        private const val PICTURE_URL = "from_url"

        @JvmStatic
        fun fromDownloadUrl(context: Context, url: String) : Intent {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putExtra(PICTURE_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        Glide.with(this).load(intent.getStringExtra(PICTURE_URL))
                .into(findViewById(R.id.imageContent))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        this.finish()
    }
}
