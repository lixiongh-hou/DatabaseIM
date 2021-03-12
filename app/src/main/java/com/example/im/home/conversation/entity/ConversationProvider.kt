package com.example.im.home.conversation.entity

import android.util.Log
import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.adapter.IConversationProvider
import com.google.gson.Gson
import java.util.ArrayList

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ConversationProvider : IConversationProvider {
    private val mDataSource: ArrayList<ConversationEntity> = ArrayList()
    private var mAdapter: ConversationListAdapter? = null


    override fun getDataSource(): MutableList<ConversationEntity> = mDataSource

    override fun attachAdapter(adapter: IConversationAdapter?) {
        mAdapter = (adapter as ConversationListAdapter?)
    }

    /**
     * 设置会话数据源
     *
     * @param dataSource
     */
    fun setDataSource(dataSource: List<ConversationEntity>) {
        Log.e("测试", "dataSource:"+Gson().toJson(dataSource))
        mDataSource.clear()
        mDataSource.addAll(dataSource)
        updateAdapter()
    }

    /**
     * 删除单个会话数据
     *
     * @param index 会话在数据源集合的索引
     * @return
     */
    fun deleteConversation(index: Int) {
        mDataSource.removeAt(index)
        updateAdapter()
    }


    /**
     * 清空会话
     */
    fun clear() {
        mDataSource.clear()
        updateAdapter()
        mAdapter = null
    }

    /**
     * 会话会话列界面，在数据源更新的地方调用
     */
    private fun updateAdapter() {
        if (mAdapter != null) {
            mAdapter?.notifyDataSetChanged()
        }
    }


}