package com.example.im.home.conversation

import android.util.Log
import com.example.im.db.CommonDatabase
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.conversation.entity.ConversationProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                        CommonDatabase.database.conversationDao()
                            .save(ConversationEntity.generatePoConversation(entity))
                        CommonDatabase.database.messageDao().save(entity.lastMessage!!)
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
                        CommonDatabase.database.conversationDao().updateModifyUnread(count, it.id)
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
                        .updateModifyTop(conversation.top, conversation.id)
                } catch (e: Exception) {
                    Log.e("测试", "置顶失败:::$e")
                }
            }
        }
        mProvider?.setDataSource(sortConversations(mProvider!!.getDataSource()))
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