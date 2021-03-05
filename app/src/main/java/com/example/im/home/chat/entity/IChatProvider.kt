package com.example.im.home.chat.entity

import com.example.im.home.chat.adapter.MessageListAdapter

/**
 * @author 李雄厚
 *
 * @features ***
 */
interface IChatProvider {

    /**
     * 获取聊天消息数据
     *
     * @return
     */
    fun getDataSource(): MutableList<PoMessageEntity>

    /**
     * 批量添加聊天消息
     *
     * @param messages 聊天消息
     * @param front    是否往前加（前：消息列表的头部，对应聊天界面的顶部，后：消息列表的尾部，对应聊天界面的底部）
     * @return
     */
    fun addMessageList(messages: MutableList<PoMessageEntity>, front: Boolean): Boolean


    /**
     * 绑定会话适配器时触发的调用
     *
     * @param adapter 会话UI显示适配器
     */
    fun setAdapter(adapter: MessageListAdapter?)
}