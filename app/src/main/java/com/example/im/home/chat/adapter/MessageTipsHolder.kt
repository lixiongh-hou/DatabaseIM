package com.example.im.home.chat.adapter

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MessageTipsHolder(itemView: View) : MessageEmptyHolder(itemView) {
    private var mChatTipsTv: TextView? = null


    override fun getVariableLayout(): Int = R.layout.message_adapter_content_tips

    override fun initVariableViews() {
        mChatTipsTv = rootView.findViewById(R.id.chat_tips_tv)
    }

    override fun layoutViews(msg: PoMessageEntity?, position: Int) {
        super.layoutViews(msg, position)

        if (msg?.status == PoMessageEntity.MSG_STATUS_REVOKE) {
            if (msg.self) {
                msg.extra = "您撤回了一条消息"
            } else {
                msg.extra = "对方撤回了一条消息"
            }
        }

        if (msg!!.status == PoMessageEntity.MSG_STATUS_REVOKE
            || (msg.status >= PoMessageEntity.MSG_TYPE_GROUP_CREATE
                    && msg.status <= PoMessageEntity.MSG_TYPE_GROUP_AV_CALL_NOTICE)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mChatTipsTv?.text = Html.fromHtml(msg.extra, Html.FROM_HTML_MODE_LEGACY)
            } else {
                mChatTipsTv?.text = Html.fromHtml(msg.extra)
            }
        }
    }
}