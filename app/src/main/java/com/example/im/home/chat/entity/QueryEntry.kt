package com.example.im.home.chat.entity

/**
 * @author 李雄厚
 *
 * @features ***
 */
data class QueryEntry(
    /**
     * 对方名字
     */
    val title: String,
    /**
     * 对方id
     */
    val formUser: String,
    /**
     * 对方头像
     */
    val iconUrl: String,
    /**
     * 是否保持本地
     */
    val saveLocal: Boolean

) : java.io.Serializable