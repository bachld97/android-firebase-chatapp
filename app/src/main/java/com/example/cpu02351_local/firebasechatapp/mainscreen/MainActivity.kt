package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.cpu02351_local.firebasechatapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mAdapter: AppFragmentPagerAdapter
    private lateinit var mLoggedInUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLoggedInUser = intent.getStringExtra("loggedInUser")
        mViewPager = findViewById(R.id.viewPager)
        mViewPager.offscreenPageLimit  = 2
        mTabLayout = findViewById(R.id.tabLayout)
        mAdapter = AppFragmentPagerAdapter(supportFragmentManager, mLoggedInUser)
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }

}


