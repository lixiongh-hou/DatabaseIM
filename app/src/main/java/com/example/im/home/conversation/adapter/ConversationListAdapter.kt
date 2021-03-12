package com.example.im.home.conversation.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.utli.NonDoubleClick.clickWithTrigger
import com.example.im.databinding.ConversationAdapterBinding
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.conversation.entity.ConversationProvider
import com.example.im.util.EmotionManager

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ConversationListAdapter : IConversationAdapter() {

    private var mDataSource: MutableList<ConversationEntity> = ArrayList()

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    fun setOnItemLongClickListener(mOnItemLongClickListener: OnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener
    }

    override fun setDataProvider(provider: IConversationProvider) {
        mDataSource = provider.getDataSource()
        if (provider is ConversationProvider) {
            provider.attachAdapter(this)
        }
        this.notifyDataSetChanged()
    }

    override fun getItem(position: Int): ConversationEntity? {
        return if (mDataSource.size == 0) null else mDataSource[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationCommonHolder =
        ConversationCommonHolder(
            ConversationAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = mDataSource.size

    override fun onBindViewHolder(holder: ConversationCommonHolder, position: Int) {
        holder.binding as ConversationAdapterBinding
        holder.binding.data = mDataSource[position]
        EmotionManager.handlerEmotionText(holder.binding.conversationLastMsg, mDataSource[position].lastMessage!!.extra, false)
        //设置点击和长按事件
        holder.binding.root.clickWithTrigger {
            mOnItemClickListener?.invoke(it, position, mDataSource[position])
        }
        holder.binding.root.setOnLongClickListener {
            mOnItemLongClickListener?.invoke(it, position, mDataSource[position])
            true
        }
    }

    fun notifyDataSourceChanged(conversationId: String?) {
        for (i in mDataSource.indices) {
            if (TextUtils.equals(conversationId, mDataSource[i].conversationId)) {
                notifyItemChanged(i)
                return
            }
        }
    }
}

typealias OnItemClickListener = (view: View?, position: Int, messageInfo: ConversationEntity) -> Unit
typealias OnItemLongClickListener = (view: View?, position: Int, messageInfo: ConversationEntity) -> Unit