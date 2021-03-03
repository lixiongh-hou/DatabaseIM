package com.example.im.home.conversation.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Entity(tableName = "conversation")
data class PoConversationEntity(
    /**
     * 消息未读数
     */
    val unRead: Int,
    /**
     * 会话标识 C2C为对方用户ID，群聊为群组ID
     */
    val id: String,
    /**
     * 会话id
     */
    val conversationId: String,
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
    val top: Boolean,
    /**
     * 最后一条消息时间
     */
    val lastMessageTime: Long
){
    /**
     * 主解ID
     */
    @PrimaryKey(autoGenerate = true)
    var keyId: Int = 0
}