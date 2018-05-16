package com.example.cpu02351_local.firebasechatapp.loginscreen

import android.content.Context

class LogInHelper {
    companion object {
        private const val FILE_PREF = "mySharedPref"
        private const val PREF_MODE = Context.MODE_PRIVATE
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_LOGGED_IN_USER = "loggedInUser"

        @JvmStatic
        fun isLoggedIn(context: Context): Boolean {
            return context.getSharedPreferences(FILE_PREF, PREF_MODE)
                    .getBoolean(KEY_IS_LOGGED_IN, false)
        }

        @JvmStatic
        fun getLoggedInUser(context: Context): String {
            return context.getSharedPreferences(FILE_PREF, PREF_MODE)
                    .getString(KEY_LOGGED_IN_USER, "")
        }

        @JvmStatic
        private fun setLoggedInUser(context: Context, username: String) {
            context.getSharedPreferences(FILE_PREF, PREF_MODE)
                    .edit().putString(KEY_LOGGED_IN_USER, username)
                    .apply()
        }

        @JvmStatic
        private fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
            context.getSharedPreferences(FILE_PREF, PREF_MODE)
                    .edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
                    .apply()
        }

        @JvmStatic
        fun logIn(context: Context, username: String) {
            setLoggedIn(context, true)
            setLoggedInUser(context, username)
        }

        @JvmStatic
        fun logOut(context:Context) {
            setLoggedIn(context, false)
            setLoggedInUser(context, "")
        }
    }
}