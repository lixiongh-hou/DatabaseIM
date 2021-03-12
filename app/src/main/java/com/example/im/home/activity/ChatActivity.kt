package com.example.im.home.activity

import Const
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.base.base.BaseActivity
import com.example.im.R
import com.example.im.databinding.ActivityChatBinding
import com.example.im.home.fragment.ChatFragment
import com.example.im.util.FileUtil
import com.example.im.view.PanelAddViewPager

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ChatActivity : BaseActivity<ActivityChatBinding>() {
    override fun initLayout(): Int = R.layout.activity_chat

    override fun initView() {
        supportFragmentManager.beginTransaction().replace(
            R.id.ffContent,
            ChatFragment.instance(intent.getSerializableExtra(Const.INFO)!!)
        ).commitAllowingStateLoss()
    }
}