package com.example.im.home.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.base.base.BaseApp
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class MessageBaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mAdapter: MessageListAdapter? = null
    protected var rootView: View = itemView
    protected var messageLongClick: OnMessageLongClick? = null
    protected var userIconClick: OnUserIconClick? = null

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        mAdapter = adapter as MessageListAdapter
    }

    fun onMessageLongClick(messageLongClick: OnMessageLongClick?) {
        this.messageLongClick = messageLongClick
    }

    fun onUserIconClick(userIconClick: OnUserIconClick?) {
        this.userIconClick = userIconClick
    }

    abstract fun layoutViews(msg: PoMessageEntity?, position: Int)

    object Factory {
        fun getInstance(
            parent: ViewGroup,
            adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
            viewType: Int
        ): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(BaseApp.instance)
            var holder: RecyclerView.ViewHolder? = null
            var view: View?

            //头部Holder
            if (viewType == MessageListAdapter.MSG_TYPE_HEADER_VIEW) {
                view = inflater.inflate(R.layout.message_adapter_content_header, parent, false)
                holder = MessageHeaderHolder(view)
                return holder
            }
            // 加群消息等Holder
            if (viewType > PoMessageEntity.MSG_TYPE_TIPS) {
                view = inflater.inflate(R.layout.message_adapter_item_empty, parent, false)
                holder = MessageTipsHolder(view)
            }

            // 具体消息holder
            view = inflater.inflate(R.layout.message_adapter_item_content, parent, false)
            when (viewType) {
                PoMessageEntity.MSG_TYPE_TEXT -> holder = MessageTextHolder(view)
            }

            if (holder != null) {
                (holder as MessageEmptyHolder).setAdapter(adapter)
            }

            return holder!!
        }
    }

}