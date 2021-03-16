package com.example.im.home.activity

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.FrameLayout
import com.example.base.base.BaseActivity
import com.example.im.R
import com.example.im.databinding.ActivityPersonalInformationBinding
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.chat.entity.QueryEntry
import com.google.gson.Gson

/**
 * @author 李雄厚
 *
 * @features ***
 */
class PersonalInformationActivity : BaseActivity<ActivityPersonalInformationBinding>() {
    private var info: QueryEntry? = null

    private val colors by lazy {
        val color: MutableList<Int> = ArrayList()
        color.add(Color.parseColor("#FFDC143C"))
        color.add(Color.parseColor("#FFFF7F50"))
        color.add(Color.parseColor("#FF00FFFF"))
        color.add(Color.parseColor("#FFFF1493"))
        color
    }
    private val views by lazy {
        val view: MutableList<FrameLayout> = ArrayList()
        view.add(mBinding.frameLayout1)
        view.add(mBinding.frameLayout2)
        view.add(mBinding.frameLayout3)
        view.add(mBinding.frameLayout4)
        view
    }

    private var index = 0
    private var mHandler: Handler? = null

    override fun initLayout(): Int = R.layout.activity_personal_information

    override fun initView() {
        mBinding.activity = this
        intent?.let {
            val str = it.getStringExtra(Const.INFO)
            info = Gson().fromJson(str, QueryEntry::class.java)
        }
        setMyTitle(info?.title ?: "")
        mBinding.data = info

        for (i in views.indices) {
            views[i].setBackgroundColor(colors[i])

        }
        mHandler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                if (msg.what == 0x001) {
                    for (i in views.indices) {
                        views[i].setBackgroundColor(colors[(i + index) % views.size])
                        Log.e("测试", "index${(i + index) % views.size}")
                    }
                    index++
                }
                super.handleMessage(msg)
            }
        }
    }

    fun confirm() {
//        val intent = Intent(this, ChatActivity::class.java)
//        intent.putExtra(Const.INFO, info)
//        startActivity(intent)
        Thread(Runnable {
            run {
                while (true) {
                    Thread.sleep(2000)
                    val message = Message.obtain()
                    message.what = 0x001
                    mHandler?.sendMessage(message)
                }
            }
        }).start()

    }
}