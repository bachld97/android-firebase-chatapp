package com.example.cpu02351_local.firebasechatapp.ChatView.UserDetail

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ViewObserver.UserDetailViewObserver
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.model.User
import com.example.cpu02351_local.firebasechatapp.GlideDataBinder
import com.example.cpu02351_local.firebasechatapp.R
import android.provider.MediaStore
import android.graphics.Bitmap
import android.R.attr.data
import android.util.Log
import java.io.IOException


class UserDetailFragment :
        UserDetailViewObserver,
        Fragment() {

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mAvatar: ImageView

    companion object {

        const val PICK_IMAGE = 113
        const val STORAGE_PERM = 114

        @JvmStatic
        fun newInstance(mViewModel: ChatViewModel): UserDetailFragment {
            val res = UserDetailFragment()
            res.mViewModel = mViewModel
            return res
        }
    }

    override fun onUserDetailLoaded(user: User) {
        GlideDataBinder.setImageUrl(mAvatar, user.avaUrl)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragmnent_user_deatil, container, false)
        mAvatar = v.findViewById(R.id.avatar)
        mAvatar.setOnClickListener {
            chooseNewAva()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            val filePath = data?.data
            if (filePath != null) {
                mViewModel.saveImage(filePath)
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        mViewModel.register(this)
    }

    override fun onStop() {
        mViewModel.unregister(this)
        super.onStop()
    }
}