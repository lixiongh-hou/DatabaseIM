package com.example.im.home.conversation

import android.util.Log
import com.example.im.db.CommonDatabase
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.chat.entity.QueryEntry
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.conversation.entity.ConversationProvider
import com.example.im.home.fragment.ChatFragment
import com.example.im.util.SetUpFieldUtil
import com.example.im.util.UserUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ConversationManagerKit {

    var mProvider: ConversationProvider? = null

    /**
     * 在数据库中生成一条会话和一条对应的聊天消息
     */
    fun saveConversation(entity: ConversationEntity, saveLocal: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (saveLocal) {
                    CommonDatabase.database.runInTransaction {
                        val it = CommonDatabase.database.conversationDao().queryCount(entity.conversationId)
                        CommonDatabase.database.conversationDao()
                            .save(ConversationEntity.generatePoConversation(entity))
                    }
                    loadConversation(null)
                } else {
                    withContext(Dispatchers.Main) {
                        val list: MutableList<ConversationEntity> = ArrayList()
                        list.addAll(mProvider!!.getDataSource())
                        list.add(entity)
                        mProvider?.setDataSource(sortConversations(list))
                    }
                }
            } catch (e: Exception) {
                Log.e("测试", "生成一条会话消息错误：$e")
            }
        }


    }

    fun loadConversation(success: ((ConversationProvider) -> Unit)?) {
        if (mProvider == null) {
            mProvider = ConversationProvider()
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val conversation: MutableList<ConversationEntity> = ArrayList()
                CommonDatabase.database.runInTransaction {
                    val poConversation = CommonDatabase.database.conversationDao().queryAll()
                    poConversation.forEach {
                        val poMessage =
                            CommonDatabase.database.messageDao().queryFromUserMessage(it.id)
                        val count = CommonDatabase.database.messageDao().queryCount(it.id)
                        CommonDatabase.database.conversationDao().updateModifyUnread(count, it.conversationId)
                        conversation.add(
                            ConversationEntity(
                                count,
                                it.conversationId,
                                it.id,
                                it.iconUrl,
                                it.title,
                                it.isGroup,
                                it.top,
                                poMessage[poMessage.size - 1].msgTime,
                                poMessage[poMessage.size - 1]
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    mProvider?.setDataSource(sortConversations(conversation))
                }
                success?.invoke(mProvider!!)
            } catch (e: Exception) {
                Log.e("测试", "加载会话消息错误：$e")
            }
        }
    }

    /**
     * 会话排序
     */
    private fun sortConversations(entity: MutableList<ConversationEntity>): MutableList<ConversationEntity> {
        val conversationInfo: MutableList<ConversationEntity> = ArrayList()
        val normalConversations: MutableList<ConversationEntity> = ArrayList()
        val topConversations: MutableList<ConversationEntity> = ArrayList()
        entity.forEach {
            if (it.top) {
                topConversations.add(it)
            } else {
                normalConversations.add(it)
            }
        }
        if (topConversations.size > 1 && topConversations.isNotEmpty()) {
            topConversations.sort()
        }
        conversationInfo.addAll(topConversations)
        if (normalConversations.size > 1 && normalConversations.isNotEmpty()) {
            normalConversations.sort()
        }
        conversationInfo.addAll(normalConversations)
        return conversationInfo
    }


    /**
     * 将某个会话置顶
     */
    fun setConversationTop(position: Int, conversation: ConversationEntity) {
        Log.e("测试", "置顶操作，位置${position}，数据：" + Gson().toJson(conversation))
        conversation.top = !conversation.top
        if (conversation.saveLocal) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    CommonDatabase.database.conversationDao()
                        .updateModifyTop(conversation.top, conversation.conversationId)
                } catch (e: Exception) {
                    Log.e("测试", "置顶失败:::$e")
                }
            }
        }
        mProvider?.setDataSource(sortConversations(mProvider!!.getDataSource()))
    }

    /**
     * 删除某个会话
     */
    fun setConversationDelete(position: Int, conversation: ConversationEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                CommonDatabase.database.conversationDao().deleteConversation(conversation.id)
            } catch (e: Exception) {
                Log.e("测试", "删除会话失败$e")
            }
        }
        mProvider?.deleteConversation(position)

    }

    /**
     * 获取数据库中是否存在和对方聊天的会话，没有就生成一条
     */
    fun getConversationCount(info: QueryEntry, content: String, self: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val it = CommonDatabase.database.conversationDao().queryCount(info.formUser)
                Log.e("测试", it.toString())
                if (SetUpFieldUtil.getField().localMsg!! && info.saveLocal) {
                    if (it <= 0) {
                        val entity = ConversationEntity(
                            1, UUID.randomUUID().toString(),
                            info.formUser, info.iconUrl, info.title,
                            false, false, System.currentTimeMillis(),
                            PoMessageEntity(
                                info.formUser,
                                UUID.randomUUID().toString(),
                                PoMessageEntity.MSG_TYPE_TEXT,
                                PoMessageEntity.MSG_STATUS_SEND_SUCCESS,
                                self,
                                true,
                                "",
                                content,
                                if (self) UserUtil.getMyHead() else info.iconUrl,
                                0,
                                0,
                                System.currentTimeMillis(),
                                true,
                                true
                            )
                        )
                        entity.saveLocal = info.saveLocal
                        saveConversation(entity, info.saveLocal)
                    }
                }
            } catch (e: Exception) {
                Log.e("测试", "查询会话数量错误$e")
            }
        }
    }

    /**
     * 与UI做解绑操作，避免内存泄漏
     */
    fun destroyConversation() {
        if (mProvider != null) {
            mProvider?.attachAdapter(null)
        }
    }
}