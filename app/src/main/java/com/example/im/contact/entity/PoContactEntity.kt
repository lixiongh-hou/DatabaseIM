package com.example.im.contact.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Entity(tableName = "contact")
data class PoContactEntity(
    /**
     * 用户ID
     */
    val id: String,
    /**
     * 用户名称
     */
    val title: String,
    /**
     * 用户头像
     */
    val faceUrl: String

) {
    /**
     * 主键ID
     */
    @PrimaryKey(autoGenerate = true)
    var keyId: Int = 0
}