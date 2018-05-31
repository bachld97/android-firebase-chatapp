package com.example.cpu02351_local.firebasechatapp.mainscreen

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.localdatabase.DaggerRoomLocalDatabaseComponent
import com.example.cpu02351_local.firebasechatapp.localdatabase.RoomLocalDatabase
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactItem
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.ContactLoader
import com.example.cpu02351_local.firebasechatapp.mainscreen.contactlist.FirebaseContactLoader
import com.example.cpu02351_local.firebasechatapp.utils.ContactConsumerView
import com.example.cpu02351_local.firebasechatapp.utils.ContactItemEvent
import com.example.cpu02351_local.firebasechatapp.utils.ContactProducerViewModel
import com.example.cpu02351_local.firebasechatapp.utils.ContextModule
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class MainActivity :
        ContactConsumerView,
        AppCompatActivity() {
    override fun onNetworkContactsLoaded(res: List<ContactItem>) {
        contactItems = res
        hasResultFromNetwork = true
        notifyData()
    }

    override fun onLocalContactsLoaded(res: List<ContactItem>) {
        if (hasResultFromNetwork)
            contactItems = res
        notifyData()
    }

    private var contactItems: List<ContactItem>? = null

    private var hasResultFromNetwork = false
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private var mAdapter: AppFragmentPagerAdapter? = null
    private lateinit var mLoggedInUser: String
    private var mContactLoader: ContactLoader = FirebaseContactLoader()
    private lateinit var mContactProducerViewModel: ContactProducerViewModel

    @Inject
    lateinit var mLocalDatabase: RoomLocalDatabase

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
        mContactProducerViewModel = ContactProducerViewModel(mContactLoader,
                this,
                mLoggedInUser)

        DaggerRoomLocalDatabaseComponent
                .builder()
                .contextModule(ContextModule(this))
                .build()
                .injectInto(this)

        mContactProducerViewModel.setLocalUserDatabase(mLocalDatabase)
        notifyData()
    }

    private fun notifyData() {
        if (contactItems == null)
            return

        if (hasResultFromNetwork)
            EventBus.getDefault().post(ContactItemEvent(contactItems!!, true))
        else
            EventBus.getDefault().post(ContactItemEvent(contactItems!!, false))
    }

    override fun onStart() {
        super.onStart()
        mContactProducerViewModel.resume()
    }

    override fun onStop() {
        super.onStop()
        mContactProducerViewModel.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        mContactProducerViewModel.dispose()
    }

}


