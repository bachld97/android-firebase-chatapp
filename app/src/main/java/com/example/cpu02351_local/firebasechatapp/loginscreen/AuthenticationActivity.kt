package com.example.cpu02351_local.firebasechatapp.loginscreen

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.ChatView.MainActivity
import com.example.cpu02351_local.firebasechatapp.R
import com.example.cpu02351_local.firebasechatapp.databinding.ActivityAppLaunchBinding

class AuthenticationActivity :
        AuthenticationCallback,
        AppCompatActivity() {

    override fun onStop() {
        mViewModel.dispose()
        super.onStop()
    }

    private var isLoggedIn = false
    private var mLoggedInUser = ""
    private val mAuthenticator: ChatAuthenticator = FirebaseChatAuthenticator()
    private val mViewModel = AuthenticationViewModel(mAuthenticator, this)

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

    override fun onAuthenticationSuccess(approvedUser: String) {
        performLogInFor(approvedUser)
    }

    override fun onAuthenticationError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
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
