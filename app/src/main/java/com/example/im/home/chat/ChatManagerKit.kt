package com.example.im.home.chat

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.im.db.CommonDatabase
import com.example.im.home.chat.entity.ChatProvider
import com.example.im.home.chat.entity.PageResponse
import com.example.im.home.chat.entity.PoMessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ChatManagerKit {

    private var mProvider: ChatProvider? = null


    fun loadConversation(
        page: String, id: String, first: Boolean,
        success: (PageResponse<PoMessageEntity>) -> Unit,
        callBack: (ChatProvider) -> Unit
    ) {
        if (mProvider == null) {
            mProvider = ChatProvider()
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                var response: PageResponse<PoMessageEntity>? = null
                CommonDatabase.database.runInTransaction {
                    val offset = (page.toInt() - 1) * 15
                    val sql = StringBuilder()
                    sql.append("SELECT * FROM message WHERE 1=1 AND fromUser IN ( '${id}' ) ORDER BY keyId+0 desc limit ${offset},15  ")
                    val queryCount = CommonDatabase.database.messageDao().queryCount(id)
                    val poList = CommonDatabase.database.messageDao().queryPageMessage(
                        SimpleSQLiteQuery(sql.toString())
                    )
                    response = PageResponse(
                        page.toInt(),
                        poList.size,
                        queryCount / 15 + 1,
                        queryCount,
                        15,
                        poList
                    )
                }
                if (response == null) {
                    response = PageResponse(0, 0, 0, 0, 0, Collections.emptyList())
                }
                success.invoke(response!!)
                response!!.lists.reverse()
                mProvider?.addMessageList(response!!.lists, true)
                if (first) {
                    callBack.invoke(mProvider!!)
                }
            } catch (e: Exception) {
                Log.e("测试", "消息查询错误$e")
            }
        }
    }

    fun saveMessage(entity: PoMessageEntity, retry: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (entity.msgType < PoMessageEntity.MSG_TYPE_TIPS) {
                    entity.status = PoMessageEntity.MSG_STATUS_NORMAL
                    if (retry) {
                        mProvider?.resendMessageInfo(entity)
                    } else {
                        mProvider?.addMessageInfo(entity)
                    }
                }
            } catch (e: Exception) {
                Log.e("测试", "消息发送失败")
            }
        }
    }

}