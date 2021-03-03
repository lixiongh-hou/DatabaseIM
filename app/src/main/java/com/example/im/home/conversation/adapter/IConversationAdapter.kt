package com.example.im.home.conversation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.im.home.conversation.entity.ConversationEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class IConversationAdapter : RecyclerView.Adapter<ConversationCommonHolder>() {

    /**
     * 设置适配器的数据源，该接口一般由ConversationContainer自动调用
     */
    abstract fun setDataProvider(provider: IConversationProvider)

    /**
     * 获取适配器的条目数据，返回的是ConversationInfo对象或其子对象
     */
    abstract fun getItem(position: Int): ConversationEntity?
}