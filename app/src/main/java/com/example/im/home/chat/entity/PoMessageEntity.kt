package com.example.im.home.chat.entity

/**
 * @author 李雄厚
 *
 * @features ***
 */
data class PoMessageEntity (
    /**
     * 对方ID
     */
    val fromUserId: String,

    /**
     * 文字说明
     */
    var extra: String
)