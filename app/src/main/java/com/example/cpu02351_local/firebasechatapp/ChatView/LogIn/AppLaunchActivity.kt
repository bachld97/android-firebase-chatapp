package com.example.cpu02351_local.firebasechatapp.ChatView.LogIn

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.ChatDataSource.FirebaseChatAuthenticator
import com.example.cpu02351_local.firebasechatapp.ChatView.MainActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.AuthenticateViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.AuthenticationCallback
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.Authentication.ChatAuthenticator
import com.example.cpu02351_local.firebasechatapp.LogInHelper
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ActivityAppLaunchBinding
import io.reactivex.Single

class AppLaunchActivity :
        AuthenticationCallback,
        AppCompatActivity() {

    private var isLoggedIn = false
    private var mLoggedInUser = ""
    private val mAuthenticator: ChatAuthenticator = FirebaseChatAuthenticator()
    private val mViewModel = AuthenticateViewModel(mAuthenticator, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLoggedInInformation()
        if (isLoggedIn && mLoggedInUser.isNotEmpty()) {
            logIn()
        } else {
            val binding = DataBindingUtil.setContentView<ActivityAppLaunchBinding>(this, R.layout.activity_app_launch)
            binding.viewModel = mViewModel
            binding.executePendingBindings()
        }
    }

    override fun onCallbackResult(result: Single<String>) {
        result.subscribe(
                { approvedUserId -> performLogInFor(approvedUserId) },
                { error -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() })
    }

    private fun performLogInFor(username: String) {
        mLoggedInUser = username
        LogInHelper.logIn(applicationContext, username)
        logIn()
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
