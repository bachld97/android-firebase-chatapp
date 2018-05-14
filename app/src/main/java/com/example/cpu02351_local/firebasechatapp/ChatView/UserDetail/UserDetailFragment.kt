package com.example.cpu02351_local.firebasechatapp.ChatView.UserDetail

import android.support.v4.app.Fragment
import com.example.cpu02351_local.firebasechatapp.ChatViewModel.ChatViewModel

class UserDetailFragment : Fragment() {
    private lateinit var mViewModel: ChatViewModel

    companion object {
        @JvmStatic
        fun newInstance(mViewModel: ChatViewModel): UserDetailFragment {
            val res = UserDetailFragment()
            res.mViewModel = mViewModel
            return res
        }
    }

}