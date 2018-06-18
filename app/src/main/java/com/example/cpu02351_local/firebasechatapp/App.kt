package com.example.cpu02351_local.firebasechatapp

import android.app.Application
import android.support.annotation.Nullable
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.upstream.TransferListener
import com.devbrackets.android.exomedia.ExoMedia
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import okhttp3.OkHttpClient
import java.io.File
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        configureExoMedia()
    }

    private fun configureExoMedia() {
        ExoMedia.setDataSourceFactoryProvider(object : ExoMedia.DataSourceFactoryProvider {
            override fun provide(userAgent: String, listener: TransferListener<in DataSource>?): DataSource.Factory {
                if (instance == null) {
                    // Updates the network data source to use the OKHttp implementation
                    val upstreamFactory = OkHttpDataSourceFactory(OkHttpClient(), userAgent, listener)

                    // Adds a cache around the upstreamFactory
                    val cache = SimpleCache(File(cacheDir, "ExoMediaCache"), LeastRecentlyUsedCacheEvictor((50 * 1024 * 1024).toLong()))
                    instance = CacheDataSourceFactory(cache, upstreamFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
                }

                return instance!!
            }

            @Nullable
            private var instance: CacheDataSourceFactory? = null
        })
    }
}