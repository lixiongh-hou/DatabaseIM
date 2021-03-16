package com.example.im.home.chat

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.base.base.BaseApp
import com.example.base.utli.ToastUtil.toast
import com.example.im.db.CommonDatabase
import com.example.im.home.chat.entity.ChatProvider
import com.example.im.home.chat.entity.PageResponse
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.ConversationManagerKit
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.util.SetUpFieldUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ChatManagerKit {

    private var mProvider: ChatProvider? = null

    fun setProvider() {
        mProvider = ChatProvider()
    }

    fun loadConversation(
        page: String, id: String, first: Boolean,
        success: (PageResponse<PoMessageEntity>) -> Unit,
        callBack: (ChatProvider) -> Unit
    ) {
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

    fun saveMessage(entity: PoMessageEntity, retry: Boolean, saveLocal: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            if (entity.msgType < PoMessageEntity.MSG_TYPE_TIPS) {
                entity.status =
                    if (entity.self) PoMessageEntity.MSG_STATUS_NORMAL else PoMessageEntity.MSG_STATUS_SEND_SUCCESS
                if (retry) {
                    mProvider?.resendMessageInfo(entity)
                } else {
                    mProvider?.addMessageInfo(entity)
                }
            }
            if (entity.self) {
                delay(300)
            }
            try {
                if (entity.self) {
                    entity.status =
                        if (SetUpFieldUtil.getField().msgSuccessOrFailure!!) PoMessageEntity.MSG_STATUS_SEND_SUCCESS else PoMessageEntity.MSG_STATUS_SEND_FAIL
                    mProvider?.updateMessageInfo(entity)
                }
                if (saveLocal) {
                    if (SetUpFieldUtil.getField().localMsg!!) {
                        CommonDatabase.database.messageDao().save(entity)
                        ConversationManagerKit.loadConversation(null)
                    }
                }
            } catch (e: Exception) {
                Log.e("测试", "消息发送失败")
            }
        }
    }

    fun deleteMessage(position: Int, message: PoMessageEntity) {
        Log.e("测试", "删除一条消息，位置${position}，数据：" + Gson().toJson(message))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                mProvider?.removeMessageInfo(message, position)
                if (SetUpFieldUtil.getField().deleteMessage!!) {
                    CommonDatabase.database.messageDao().deleteMessage(message.msgId)
                    ConversationManagerKit.loadConversation(null)
                }
            } catch (e: Exception) {
                Log.e("测试", "删除消息失败:::$e")
            }
        }
    }

    fun revokeMessage(position: Int, message: PoMessageEntity) {
        Log.e("测试", "撤销消息，位置${position}，数据：" + Gson().toJson(message))
        GlobalScope.launch(Dispatchers.IO) {
            try {
                //当前时间-消息发送的时间
                val time = System.currentTimeMillis() - message.msgTime
                //本吧保持的消息撤回分钟转换成毫秒
                val millisecond =
                    (SetUpFieldUtil.getField().revokeMessageTime!! * 60 * 1000).toLong()
                if (time < millisecond || !SetUpFieldUtil.getField().revokeMessage!!) {
                    mProvider?.updateMessageRevoked(message)
                    if (SetUpFieldUtil.getField().revokeMessageLocal!!) {
                        CommonDatabase.database.messageDao().revokeMessage(
                            message.msgId,
                            PoMessageEntity.MSG_STATUS_REVOKE,
                            PoMessageEntity.MSG_STATUS_REVOKE,
                            if (message.self) "您撤回了一条消息" else "对方撤回了一条消息",
                            false
                        )
                        ConversationManagerKit.loadConversation(null)
                    }
                } else {
                    "消息发送超过${SetUpFieldUtil.getField().revokeMessageTime!!}分钟， 不能撤回".toast()
                }
            } catch (e: Exception) {
                Log.e("测试", "删除消息失败:::$e")
            }
        }
    }


}