package com.example.im.home.conversation.view

import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.adapter.OnItemClickListener
import com.example.im.home.conversation.adapter.OnItemLongClickListener

/**
 * @author 李雄厚
 *
 * @features ***
 */
interface IConversationListLayout {

    /**
     * 设置会话Item点击监听
     */
    fun setOnItemClickListener(listener: OnItemClickListener)

    /**
     * 设置会话Item长按监听
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener)
    /**
     * 获取会话列表Adapter
     */
    fun getAdapter(): ConversationListAdapter

    /**
     * 设置会话Adapter
     */
    fun setAdapter(adapter: IConversationAdapter)
}