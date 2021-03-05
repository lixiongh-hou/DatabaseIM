package com.example.im.home.chat.adapter

import android.view.View
import android.widget.TextView
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features 文字Holder
 */
class MessageTextHolder(itemView: View) : MessageContentHolder(itemView) {

    private var msgBodyText: TextView? = null

    override fun getVariableLayout(): Int = R.layout.message_adapter_content_text

    override fun initVariableViews() {
        msgBodyText = rootView.findViewById(R.id.msg_body_tv)
    }
    override fun layoutVariableViews(msg: PoMessageEntity?, position: Int) {
        msgBodyText?.visibility = View.VISIBLE
        if (msg?.extra != null){
            msgBodyText?.text = msg.extra
        }
    }
}