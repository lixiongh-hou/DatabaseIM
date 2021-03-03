package com.example.base.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.base.R
import kotlinx.android.synthetic.main.title_view.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity() {
    lateinit var mBinding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.navigation_bar_color)
            var vis = window.decorView.systemUiVisibility
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            vis = vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.decorView.systemUiVisibility = vis
        }
        mBinding = DataBindingUtil.setContentView(this, initLayout())
        initView()
    }

    abstract fun initLayout(): Int
    abstract fun initView()

    /*--------------------------------标题布局-------------------------------------------*/
    /**
     * 设置标题默认格式
     */
    fun setMyTitle(title: CharSequence) {
        if (tvTitle == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        } else {
            tvTitle.text = title
            ivTitleBack.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition()
                } else {
                    finish()
                }
            }
        }
    }

    fun getLiBar(): RelativeLayout {
        if (liBar == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        return liBar
    }

    fun getTitleBack(): ImageView{
        if (ivTitleBack == null){
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        return ivTitleBack
    }

    /**
     * 获取标题右边按钮图标
     */
    fun getIvTitleRight(): ImageView {
        if (ivTitleRight == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        ivTitleRight.visibility = View.VISIBLE
        return ivTitleRight
    }

    /**
     * 获取标题左边按钮图标
     */
    fun getIvTitleLift(): ImageView {
        if (ivTitleRight == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        ivTitleRight.visibility = View.VISIBLE
        return ivTitleRight
    }

    /**
     * 获取Tv或Ev文本
     */
    fun getTvEvString(view: View): String {
        if (view is TextView) {
            return view.text.toString().trim()
        }
        if (view is EditText) {
            return view.text.toString().trim()
        }
        return ""
    }
}