package com.example.im.home.conversation.entity

import com.example.im.home.chat.entity.PoMessageEntity
import java.io.Serializable

/**
 * @author 李雄厚
 *
 * @features ***
 */
data class ConversationEntity(
    /**
     * 消息未读数
     */
    var unRead: Int,
    /**
     * 会话ID
     */
    val conversationId: String,
    /**
     * 会话标识 C2C为对方用户ID，群聊为群组ID
     */
    val id: String,
    /**
     * 会话头像url
     */
    val iconUrl: String,
    /**
     * 会话标题
     */
    val title: String,
    /**
     * 是否为群会话
     */
    val isGroup: Boolean,
    /**
     * 是否为置顶会话
     */
    var top: Boolean,
    /**
     * 最后一条消息时间
     */
    val lastMessageTime: Long,
    /**
     * 最后一条消息，MessageInfo对象
     */
    val lastMessage: PoMessageEntity?
) : Serializable, Comparable<ConversationEntity> {
    /**
     * 用于再添加会话是否要保存到本地数据库
     */
    var saveLocal: Boolean = true

    //若是当前对象比目标对象大，则返回1，那么当前对象会排在目标对象的后面
    //
    //若是当前对象比目标对象小，则返回-1，那么当前对象会排在目标对象的后面
    //
    //若是两个对象相等，则返回0
    override fun compareTo(other: ConversationEntity): Int {
        return when {
            //当前目标              //目标对象
            this.lastMessageTime > other.lastMessageTime -> {
                -1
            }
            this.lastMessageTime == other.lastMessageTime -> {
                0
            }
            else -> {
                1
            }
        }
    }

    override fun toString(): String {
        return "ConversationInfo(unRead=$unRead, conversationId='$conversationId', id='$id', iconUrl='$iconUrl', title='$title', isGroup=$isGroup, top=$top, lastMessageTime=$lastMessageTime, lastMessage=$lastMessage)"
    }
}