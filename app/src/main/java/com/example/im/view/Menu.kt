package com.example.im.view

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.im.R
import com.example.im.adapter.PopMenuAdapter
import com.example.im.entity.PopActionClickListener
import com.example.im.entity.PopMenuAction
import com.google.gson.Gson
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
class Menu(private val activity: Activity, private val attach: View) {

    private var mMenuList: RecyclerView? = null
    private var mMenuAdapter: PopMenuAdapter? = null
    private var mMenuWindow: PopupWindow? = null
    private val mActions: MutableList<PopMenuAction> = ArrayList()

    init {
        initActions()
    }

    private fun initActions(){
        val popActionClickListener = object : PopActionClickListener{
            override fun invoke(position: Int, data: Any) {
                mMenuWindow?.dismiss()
            }
        }
        // 设置右上角+号显示PopAction
        val menuActions: MutableList<PopMenuAction> = ArrayList()
        val action = PopMenuAction()
        action.actionName = "添加"
        action.setActionClickListener(popActionClickListener)
        action.iconResId = R.drawable.default_head
        menuActions.add(action)

        mActions.clear()
        mActions.addAll(menuActions)
    }

    fun isShowing(): Boolean {
        if (mMenuWindow == null) {
            return false
        }
        return mMenuWindow!!.isShowing
    }

    fun hide() {
        mMenuWindow?.dismiss()
    }

    fun show() {
        if (mActions.size == 0) {
            return
        }
        Log.e("测试", Gson().toJson(mActions))
        mMenuWindow = PopupWindow(activity)
        mMenuAdapter = PopMenuAdapter()
        mMenuAdapter?.refreshData(mActions)
        val menuView = LayoutInflater.from(activity).inflate(R.layout.conversation_pop_menu, null)
        // 设置布局文件
        mMenuWindow!!.contentView = menuView
        mMenuList = menuView.findViewById(R.id.conversation_pop_list)
        mMenuList?.layoutManager = LinearLayoutManager(activity)
        mMenuList?.adapter = mMenuAdapter
        mMenuAdapter?.clickEvent = { data, _, position ->
            val action = mMenuAdapter?.data?.get(position)
            if (action?.getActionClickListener() != null){
                action.getActionClickListener()?.invoke(position, mActions[position])
            }
        }
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mMenuWindow?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        mMenuWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置pop透明效果
        mMenuWindow?.setBackgroundDrawable(ColorDrawable(0x0000));
        // 设置pop出入动画
        mMenuWindow?.animationStyle = R.style.pop_add
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mMenuWindow?.isFocusable = true
        // 设置pop可点击，为false点击事件无效，默认为true
        mMenuWindow?.isTouchable = true
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mMenuWindow?.isOutsideTouchable = true
        // 相对于 + 号正下面，同时可以设置偏移量
        mMenuWindow?.showAsDropDown(attach, -390, 0)

    }
}