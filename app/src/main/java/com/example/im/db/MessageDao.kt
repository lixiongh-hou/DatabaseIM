package com.example.im.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Dao
interface MessageDao {
    @Insert
    fun save(entity: PoMessageEntity)

    /**
     * 查询对应用户的聊天记录
     */
    @Query("SELECT * FROM message WHERE fromUser = :fromUser")
    fun queryFromUserMessage(fromUser: String): MutableList<PoMessageEntity>

    /**
     * 查询对应用户的有效聊天记录数量 {有效表示是不是提示消息的记录，比如“您撤回一条消息”}
     */
    @Query("SELECT count(*) FROM message WHERE fromUser = :fromUser and chatMessage = 1")
    fun queryCount(fromUser: String): Int

    /**
     * 分页查询数据
     */
    @RawQuery
    fun queryPageMessage(sqLiteQuery: SupportSQLiteQuery): MutableList<PoMessageEntity>

    /**
     * 删除一条消息
     */
    @Query("DELETE FROM message WHERE msgId = :msgId")
    fun deleteMessage(msgId: String)

    /**
     * 撤回一条消息
     */
    @Query("UPDATE message SET msgType=:msgType , status=:status , extra=:extra , chatMessage=:chatMessage WHERE msgId = :msgId")
    fun revokeMessage(msgId: String, msgType: Int, status: Int, extra: String, chatMessage: Boolean)
}