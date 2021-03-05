package com.example.im

import Const
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.base.base.BaseActivity
import com.example.base.view.TabView
import com.example.im.adapter.ViewPager2Adapter
import com.example.im.databinding.ActivityMainBinding
import com.example.im.home.fragment.HomeFragment
import com.example.im.mine.fragment.MineFragment
import com.example.im.newsletter.fragment.NewsletterFragment
import com.example.im.util.LiveDataBus
import com.example.im.view.Menu

/**
 * 多少接口回调，怕忘记记一下
 */
interface TouchListener {
    fun setOnTouch(ev: MotionEvent?)
    fun setOnClick(view: View)
}

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var register: TouchListener? = null

    inner class RegisterMyTouchListener : TouchListener {
        private lateinit var onTouch: (MotionEvent?) -> Unit
        private lateinit var onClick: (View) -> Unit
        override fun setOnTouch(ev: MotionEvent?) {
            onTouch.invoke(ev)
        }

        override fun setOnClick(view: View) {
            onClick.invoke(view)
        }

        fun onTouch(onTouch: (MotionEvent?) -> Unit) {
            this.onTouch = onTouch
        }

        fun onClick(onClick: (View) -> Unit) {
            this.onClick = onClick
        }

    }

    companion object {
        const val ADD_CONVERSATION = 0x001
    }

    private val tabFragment by lazy {
        val list: MutableList<Fragment> = ArrayList()
        list.add(HomeFragment())
        list.add(NewsletterFragment())
        list.add(MineFragment())
        list
    }

    private val tabTitle by lazy {
        val list: MutableList<String> = ArrayList()
        list.add(resources.getString(R.string.home))
        list.add(resources.getString(R.string.newsletter))
        list.add(resources.getString(R.string.mine))
        list
    }

    private val tabView by lazy {
        val list: MutableList<TabView> = ArrayList()
        list.add(mBinding.mTabView1)
        list.add(mBinding.mTabView2)
        list.add(mBinding.mTabView3)
        list
    }
    private lateinit var mMenu: Menu

    override fun initLayout(): Int = R.layout.activity_main

    override fun initView() {
        setMyTitle(tabTitle[0])
        getTitleBack().visibility = View.GONE
        getIvTitleRight().setImageResource(R.drawable.add)
        mMenu = Menu(this, getIvTitleRight())
        mBinding.activity = this
        mBinding.vpAdapter = ViewPager2Adapter(this, tabFragment)
        mBinding.fragNavVp.offscreenPageLimit = tabFragment.size
        mBinding.fragNavVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                setMyTitle(tabTitle[position])
                tabView[position].setXPercentage(1 - positionOffset)
                //做个判断，防止滑动到最后一项出现下标越界
                if (position >= tabFragment.size - 1) {
                    return
                }
                tabView[position + 1].setXPercentage(positionOffset)
            }
        })

        getIvTitleRight().setOnClickListener {
            if (mMenu.isShowing()) {
                mMenu.hide()
            } else {
                mMenu.show()
            }
        }
    }

    /**
     * 更新底部导航栏
     * @param index 底部导航栏下标
     */
    fun updateCurrentTab(index: Int) {
        mBinding.fragNavVp.setCurrentItem(index, false)
        setMyTitle(tabTitle[index])
        for (i in tabFragment.indices) {
            if (i == index) {
                tabView[i].setXPercentage(1F)
            } else {
                tabView[i].setXPercentage(0F)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_CONVERSATION) {
                val info = data?.getStringExtra(Const.INFO)
                LiveDataBus.liveDataBus.with<String?>("saveConversation").setValue(info)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //将触摸事件传递给回调函数
        if (null != register) {
            register?.setOnTouch(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun registerMyTouchListener(onTouch: RegisterMyTouchListener.() -> Unit) {
        register = RegisterMyTouchListener().also(onTouch)
    }

}