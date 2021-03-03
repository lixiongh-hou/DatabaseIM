package com.example.im.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.base.base.BaseApp
import com.example.im.home.conversation.entity.PoConversationEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Database(version = 1, entities = [PoConversationEntity::class], exportSchema = true)
abstract class CommonDatabase : RoomDatabase() {

    companion object {
        val database by lazy {
            return@lazy Room.databaseBuilder(
                BaseApp.instance, CommonDatabase::class.java, "common.db"
            )
                .build()
        }
    }
    abstract fun conversationDao(): ConversationDao
}