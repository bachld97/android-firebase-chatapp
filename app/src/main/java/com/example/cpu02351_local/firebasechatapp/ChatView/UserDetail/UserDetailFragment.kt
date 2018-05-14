package com.example.cpu02351_local.firebasechatapp.ChatView.UserDetail

import android.os.Bundle
import android.support.v4.app.Fragment
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

class UserDetailFragment :
        UserDetailViewObserver,
        Fragment() {

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mAvatar: ImageView

    companion object {
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
        return v
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