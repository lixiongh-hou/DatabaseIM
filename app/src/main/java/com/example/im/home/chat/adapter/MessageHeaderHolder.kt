package com.example.im.home.chat.adapter

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features 加载中头部Holder
 */
class MessageHeaderHolder(itemView: View) : MessageBaseHolder(itemView) {

    private var mLoading = false
    fun setLoadingStatus(loading: Boolean) {
        mLoading = loading
    }

    override fun layoutViews(msg: PoMessageEntity?, position: Int) {
        val param = rootView.layoutParams as RecyclerView.LayoutParams
        if (mLoading) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT
            param.width = LinearLayout.LayoutParams.MATCH_PARENT
            rootView.visibility = View.VISIBLE
        } else {
            param.height = 0
            param.width = 0
            rootView.visibility = View.GONE
        }
        rootView.layoutParams = param
    }
}