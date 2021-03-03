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
}