package com.example.im.home.chat.entity

import com.example.im.home.chat.adapter.MessageListAdapter
import java.util.ArrayList

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ChatProvider: IChatProvider {
    private var mDataSource: MutableList<PoMessageEntity> = ArrayList()
    private var mAdapter: MessageListAdapter? = null

    override fun getDataSource(): MutableList<PoMessageEntity> = mDataSource

    override fun addMessageList(messages: MutableList<PoMessageEntity>, front: Boolean): Boolean {
        val list: MutableList<PoMessageEntity> = ArrayList()
        list.addAll(messages)
        val flag: Boolean
        if (front) {
            flag = mDataSource.addAll(0, list)
            updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_ADD_FRONT, list.size)
        } else {
            flag = mDataSource.addAll(list)
            updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_ADD_BACK, list.size)
        }
        return flag
    }

    /**
     * 添加一条消息
     */
    fun addMessageInfo(msg: PoMessageEntity?): Boolean {
        if (msg == null) {
            updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_LOAD, 0)
            return true
        }
        val flag = mDataSource.add(msg)
        updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_ADD_BACK, 1)
        return flag
    }

    /**
     * 重发消息
     */
    fun resendMessageInfo(message: PoMessageEntity): Boolean{
        var found = false
        for (i in mDataSource.indices){
            if (mDataSource[i].msgId == message.msgId) {
                mDataSource.removeAt(i)
                found = true
                break
            }
        }
        if (!found) {
            return false
        }
        return addMessageInfo(message)
    }

    private fun updateAdapter(type: Int, data: Int) {
        if (mAdapter != null) {
            mAdapter?.notifyDataSourceChanged(type, data)
        }
    }
    override fun setAdapter(adapter: MessageListAdapter?) {
        this.mAdapter = adapter
    }
}