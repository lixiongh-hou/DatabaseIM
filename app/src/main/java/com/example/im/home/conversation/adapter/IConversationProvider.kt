package com.example.im.home.conversation.adapter

import com.example.im.home.conversation.entity.ConversationEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
interface IConversationProvider {

    /**
     * 获取具体的会话数据集合，ConversationContainer依据该数据集合展示会话列表
     */
    fun getDataSource(): MutableList<ConversationEntity>

    /**
     * 绑定会话适配器时触发的调用
     */
    fun attachAdapter(adapter: IConversationAdapter?)

}