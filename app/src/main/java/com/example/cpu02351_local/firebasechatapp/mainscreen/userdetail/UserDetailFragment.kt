package com.example.cpu02351_local.firebasechatapp.mainscreen.userdetail

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.cpu02351_local.firebasechatapp.loginscreen.AuthenticationActivity
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.Utils.GlideDataBinder
import com.example.cpu02351_local.firebasechatapp.loginscreen.LogInHelper
import com.example.cpu02351_local.firebasechatapp.R


class UserDetailFragment :
        UserDetailView,
        Fragment() {

    private var mUserDetailLoader: UserDetailLoader = FirebaseUserDetailLoader()
    private lateinit var userId: String
    private lateinit var mUserDetailViewModel: UserDetailViewModel
    private lateinit var mAvatar: ImageView

    companion object {

        const val PICK_IMAGE = 113
        const val STORAGE_PERM = 114

        @JvmStatic
        fun newInstance(userId: String): UserDetailFragment {
            val temp = UserDetailFragment()
            val args = Bundle()
            args.putString("userId", userId)
            temp.arguments = args
            return temp
        }
    }

    private fun init() {
        mUserDetailViewModel = UserDetailViewModel(mUserDetailLoader, this, userId)
    }

    private fun dispose() {
        mUserDetailViewModel.dispose()
    }

    override fun onUpdateAvatarFailed() {
        Toast.makeText(context, "Failed to change profile picture", Toast.LENGTH_SHORT).show()
    }

    override fun onUserDetailLoaded(userDetail: User) {
        GlideDataBinder.setImageUrl(mAvatar, userDetail.avaUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragmnent_user_deatil, container, false)

        mAvatar = v.findViewById(R.id.avatar)
        mAvatar.setOnClickListener {
            chooseNewAva()
        }

        v.findViewById<View>(R.id.log_out).setOnClickListener {
            val intent = Intent(context, AuthenticationActivity::class.java)
            LogInHelper.logOut(activity?.applicationContext!!)
            context?.startActivity(intent)
            activity?.finish()
        }
        return v
    }

    private fun chooseNewAva() {
        if (shouldAskPermission()) {
            askPermission()
        } else {
            startImageActivity()
        }
    }

    private fun startImageActivity() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(activity!!, arrayOf(), STORAGE_PERM) // change requestCode please
    }

    private fun shouldAskPermission(): Boolean {
        return ContextCompat
                .checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == STORAGE_PERM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startImageActivity()
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        userId = args?.get("userId") as String
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            val filePath = data?.data
            if (filePath != null) {
                GlideDataBinder.setImageOffline(mAvatar, filePath)
                mUserDetailViewModel.changeAvatar(filePath)
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }
}