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
     * 更新一条消息
     */
    fun updateMessageInfo(message: PoMessageEntity): Boolean {
        for (i in mDataSource.indices) {
            if (mDataSource[i].msgId == message.msgId) {
                mDataSource.removeAt(i)
                mDataSource.add(i, message)
                updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_UPDATE, i)
                return true
            }
        }
        return false
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

    fun updateMessageRevoked(message: PoMessageEntity): Boolean{
        for (i in mDataSource.indices){
            val messageInfo: PoMessageEntity = mDataSource[i]
            if (messageInfo.msgId == message.msgId) {
                messageInfo.msgType = PoMessageEntity.MSG_STATUS_REVOKE
                messageInfo.status = PoMessageEntity.MSG_STATUS_REVOKE
                updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_UPDATE, i)
            }
        }
        return false
    }

    fun removeMessageInfo(messageInfo: PoMessageEntity, index: Int) {
        mDataSource.remove(messageInfo)
        updateAdapter(MessageListAdapter.DATA_CHANGE_TYPE_DELETE, index)
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