package com.example.im.mine.activity

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import coil.loadAny
import coil.transform.RoundedCornersTransformation
import com.example.base.base.BaseActivity
import com.example.base.utli.ScreenUtil
import com.example.im.R
import com.example.im.databinding.ActivityHeadBinding
import com.example.im.home.chat.adapter.MessageImageHolder
import com.example.im.util.UserUtil

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HeadActivity : BaseActivity<ActivityHeadBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
            val decor: View = window.decorView
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun initLayout(): Int = R.layout.activity_head

    override fun initView() {

        val lp = getLiBar().layoutParams as RelativeLayout.LayoutParams
        lp.setMargins(0, ScreenUtil.getRealStatusBarHeight(), 0, 0)
        getLiBar().layoutParams = lp
        getLiBar().setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        getTitleBack().imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        setMyTitle("头像", ContextCompat.getColor(this, R.color.white))
        getIvTitleRight().setImageResource(R.drawable.three_dots)
        getIvTitleRight().imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        getIvTitleRight().setOnClickListener {
            
        }


        mBinding.head.loadAny(
            if (UserUtil.getMyHead().startsWith("https://") ||
                UserUtil.getMyHead().startsWith("http://")
            )
                UserUtil.getMyHead() else
                MessageImageHolder.getMediaUriFromPath(this, UserUtil.getMyHead())!!
        )
    }
}