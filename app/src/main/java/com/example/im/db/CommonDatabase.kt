package com.example.im.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.base.base.BaseApp
import com.example.im.contact.entity.PoContactEntity
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.entity.PoConversationEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Database(
    version = 3,
    entities = [
        PoConversationEntity::class,
        PoMessageEntity::class,
        PoContactEntity::class
    ],
    exportSchema = true
)
abstract class CommonDatabase : RoomDatabase() {

    companion object {
        val database by lazy {
            return@lazy Room.databaseBuilder(
                BaseApp.instance, CommonDatabase::class.java, "common.db"
            )
//                .fallbackToDestructiveMigration()
                .addMigrations(object : Migration(2, 3) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE IF NOT EXISTS `contact` (`keyId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `id` TEXT NOT NULL, `title` TEXT NOT NULL, `faceUrl` TEXT NOT NULL)")
                    }

                })
                .build()
        }
    }

    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao
}