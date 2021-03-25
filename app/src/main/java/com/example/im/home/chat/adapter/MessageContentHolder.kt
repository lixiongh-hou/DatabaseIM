package com.example.im.home.chat.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import coil.load
import coil.loadAny
import coil.transform.RoundedCornersTransformation
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.util.ViewStyleUtil

/**
 * @author 李雄厚
 *
 * @features 所有消息总Holder
 */
abstract class MessageContentHolder(itemView: View) : MessageEmptyHolder(itemView) {

    var leftUserIcon: ImageView = itemView.findViewById(R.id.left_user_icon_view);
    var rightUserIcon: ImageView = itemView.findViewById(R.id.right_user_icon_view)
    var usernameText: TextView = itemView.findViewById(R.id.user_name_tv)
    var msgContentLinear: LinearLayout = itemView.findViewById(R.id.msg_content_ll)
    var sendingProgress: ProgressBar = itemView.findViewById(R.id.message_sending_pb)
    var statusImage: ImageView = itemView.findViewById(R.id.message_status_iv)
    var isReadText: TextView = itemView.findViewById(R.id.is_read_tv)
    var unreadAudioText: TextView = itemView.findViewById(R.id.audio_unread)


    override fun layoutViews(msg: PoMessageEntity?, position: Int) {
        super.layoutViews(msg, position)

        if (msg == null) {
            return
        }
        // 头像设置
        if (msg.self) {
            leftUserIcon.visibility = View.GONE
            rightUserIcon.visibility = View.VISIBLE
        } else {
            leftUserIcon.visibility = View.VISIBLE
            rightUserIcon.visibility = View.GONE
        }
        if (msg.self) {
            rightUserIcon.loadAny(
                if (msg.faceUrl.startsWith("https://") ||
                    msg.faceUrl.startsWith("http://")
                )
                    msg.faceUrl else
                    MessageImageHolder.getMediaUriFromPath(rootView.context, msg.faceUrl)!!
            ) {
                error(R.drawable.default_head)
                placeholder(R.drawable.default_head)
                transformations(RoundedCornersTransformation(10F))

            }
        } else {
            leftUserIcon.loadAny(
                if (msg.faceUrl.startsWith("https://") ||
                    msg.faceUrl.startsWith("http://")
                )
                    msg.faceUrl else
                    MessageImageHolder.getMediaUriFromPath(rootView.context, msg.faceUrl)!!
            ) {
                error(R.drawable.default_head)
                placeholder(R.drawable.default_head)
                transformations(RoundedCornersTransformation(10F))
            }
        }

        //用户昵称设置
        usernameText.visibility = View.GONE

        //发送消息进度条
        if (msg.self) {
            if (msg.status == PoMessageEntity.MSG_STATUS_SEND_FAIL || msg.status == PoMessageEntity.MSG_STATUS_SEND_SUCCESS || msg.peerRead) {
                sendingProgress.visibility = View.GONE
            } else {
                sendingProgress.visibility = View.VISIBLE
            }
        } else {
            sendingProgress.visibility = View.GONE
        }

        //设置气泡
        if (msg.self) {
            msgContentFrame?.background = ViewStyleUtil.getBackground(
                rootView.context,
                R.drawable.chat_right_bg,
                R.color.bubble_color
            )
        } else {
            msgContentFrame?.background = ViewStyleUtil.getBackground(
                rootView.context,
                R.drawable.chat_left_bg,
                R.color.white
            )
        }

        //聊天气泡的点击事件处理
        msgContentFrame?.setOnLongClickListener {
            messageLongClick?.invoke(it, position, msg)
            true
        }
        leftUserIcon.setOnClickListener {
            userIconClick?.invoke(it, position, msg)
        }
        rightUserIcon.setOnClickListener {
            userIconClick?.invoke(it, position, msg)
        }

        // 发送状态的设置
        if (msg.status == PoMessageEntity.MSG_STATUS_SEND_FAIL) {
            statusImage.visibility = View.VISIBLE
            msgContentFrame?.setOnClickListener {
                messageLongClick?.invoke(it, position, msg)
            }
        } else {
            msgContentFrame?.setOnClickListener(null)
            statusImage.visibility = View.GONE
        }

        // 左右边的消息需要调整一下内容的位置
        if (msg.self) {
            msgContentLinear.removeView(msgContentFrame)
            msgContentLinear.addView(msgContentFrame)
        } else {
            msgContentLinear.removeView(msgContentFrame)
            msgContentLinear.addView(msgContentFrame, 0)
        }
        msgContentLinear.visibility = View.VISIBLE

        // 音频已读
        unreadAudioText.visibility = View.GONE

        // 由子类设置指定消息类型的views
        layoutVariableViews(msg, position)
    }

    abstract fun layoutVariableViews(msg: PoMessageEntity?, position: Int)

}