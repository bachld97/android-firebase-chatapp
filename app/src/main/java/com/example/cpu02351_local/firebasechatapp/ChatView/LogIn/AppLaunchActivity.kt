package com.example.cpu02351_local.firebasechatapp.ChatView.LogIn

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatAuthenticator
import com.example.cpu02351_local.firebasechatapp.ChatView.MainActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatAuthenticator
import com.example.cpu02351_local.firebasechatapp.LogInHelper
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.R.id.btn_go

class AppLaunchActivity : AppCompatActivity() {

    private var isLoggedIn = false
    private var mLoggedInUser = ""

    private val mAuthenticator: ChatAuthenticator = FirebaseChatAuthenticator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLoggedInInformation()
        Thread.sleep(300)
        if (isLoggedIn && mLoggedInUser.isNotEmpty()) {
            logIn()
        } else {
            setContentView(R.layout.activity_app_launch)
        }
    }

    private fun saveLogInInformation(username: String) {
        LogInHelper.logIn(applicationContext, username)
    }

    private fun logIn() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("loggedInUser", mLoggedInUser)
        startActivity(mainIntent)
        finish()
    }

    private fun loadLoggedInInformation() {
        isLoggedIn = LogInHelper.isLoggedIn(applicationContext)
        mLoggedInUser = LogInHelper.getLoggedInUser(applicationContext)
    }
}
