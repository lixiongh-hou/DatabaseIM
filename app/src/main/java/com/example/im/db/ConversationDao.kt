package com.example.im.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.im.home.conversation.entity.PoConversationEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Dao
interface ConversationDao {
    @Insert
    fun save(patients: PoConversationEntity)

    @Query("SELECT * FROM conversation")
    fun queryAll(): MutableList<PoConversationEntity>

    /**
     * 跟新消息未读数量
     */
    @Query("UPDATE conversation SET unRead=:unRead WHERE id=:id")
    fun queryModifyUnread(unRead: Int, id: String)

    /**
     * 将某个会话置顶
     */
    @Query("UPDATE conversation SET top=:top WHERE id=:id")
    fun queryModifyTop(top: Boolean, id: String)
}