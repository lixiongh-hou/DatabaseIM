package com.example.im.home.activity

import android.text.TextUtils
import com.example.base.base.BaseActivity
import com.example.base.utli.ToastUtil.toast
import com.example.im.R
import com.example.im.databinding.ActivityChatBinding
import com.example.im.home.fragment.ChatFragment

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ChatActivity : BaseActivity<ActivityChatBinding>() {
    override fun initLayout(): Int = R.layout.activity_chat

    override fun initView() {
        if (TextUtils.isEmpty(intent.getStringExtra(Const.INFO))){
            "数据错误".toast()
            finish()
            return
        }
        supportFragmentManager.beginTransaction().replace(R.id.ffContent,
            ChatFragment.instance(intent.getStringExtra(Const.INFO)!!)).commitAllowingStateLoss();
    }
}