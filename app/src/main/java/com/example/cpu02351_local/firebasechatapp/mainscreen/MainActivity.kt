package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatView.AppFragmentPagerAdapter
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatDataSource
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var mAdapter: AppFragmentPagerAdapter
    private var mChatModel: ChatDataSource = FirebaseChatDataSource()
    private lateinit var mViewModel: ChatViewModel
    private lateinit var mLoggedInUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mLoggedInUser = intent.getStringExtra("loggedInUser")
        mViewPager = findViewById(R.id.viewPager)
        mTabLayout = findViewById(R.id.tabLayout)
        mViewModel = ChatViewModel(mChatModel, getLoggedInUser())
        mAdapter = AppFragmentPagerAdapter(supportFragmentManager, mLoggedInUser)
        mViewPager.adapter = mAdapter
        mTabLayout.setupWithViewPager(mViewPager)
     }

    override fun onStart() {
        super.onStart()
        mViewModel.init()
    }

    override fun onStop() {
        mViewModel.dispose()
        super.onStop()
    }

    private fun getLoggedInUser(): String {
        return mLoggedInUser
    }
}
