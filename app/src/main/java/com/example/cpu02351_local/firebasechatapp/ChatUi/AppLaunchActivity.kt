package com.example.cpu02351_local.firebasechatapp.ChatUi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.example.cpu02351_local.firebasechatapp.ChatUi.Adapters.AppFragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.R

class AppLaunchActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mAdapter: AppFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_launch)
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
        mAdapter = AppFragmentPagerAdapter(supportFragmentManager)
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)
     }


}
