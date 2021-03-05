package com.example.im.home.chat.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.util.DateTimeUtil

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class MessageEmptyHolder(itemView: View): MessageBaseHolder(itemView) {
    var chatTimeText: TextView? = null
    var msgContentFrame: FrameLayout? = null

    init {
        chatTimeText = itemView.findViewById(R.id.chat_time_tv)
        msgContentFrame = itemView.findViewById(R.id.msg_content_fl)
        initVariableLayout()
    }

    abstract fun getVariableLayout(): Int

    private fun initVariableLayout() {
        if (getVariableLayout() != 0) {
            setVariableLayout(getVariableLayout())
        }
    }

    private fun setVariableLayout(resId: Int) {
        if (msgContentFrame?.childCount == 0) {
            View.inflate(rootView.context, resId, msgContentFrame)
        }
        initVariableViews()
    }

    abstract fun initVariableViews()


    override fun layoutViews(msg: PoMessageEntity?, position: Int) {
        if (position > 1) {
            chatTimeText?.visibility = View.GONE
            val last = mAdapter?.getItem(position - 1)
            if (last != null) {
                if (msg!!.msgTime - last.msgTime >= 1000 * 60) {
                    chatTimeText?.visibility = View.VISIBLE
                    chatTimeText?.text = DateTimeUtil.getTimeFormatText(msg.msgTime)
                } else {
                    chatTimeText?.visibility = View.GONE
                }
            }
        } else {
            chatTimeText?.visibility = View.VISIBLE
            chatTimeText?.text = DateTimeUtil.getTimeFormatText(msg!!.msgTime)
        }

    }
}