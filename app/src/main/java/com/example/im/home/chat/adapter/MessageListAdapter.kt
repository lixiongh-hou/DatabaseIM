package com.example.im.home.chat.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.im.home.chat.entity.IChatProvider
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.util.BackgroundTasks
import com.example.im.view.HookActionUpRecyclerView

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MessageListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        /** 头部加载中布局 */
        const val MSG_TYPE_HEADER_VIEW = -99

        const val DATA_CHANGE_TYPE_REFRESH = 0
        const val DATA_CHANGE_TYPE_LOAD = 1
        const val DATA_CHANGE_TYPE_ADD_FRONT = 2
        const val DATA_CHANGE_TYPE_ADD_BACK = 3
        const val DATA_CHANGE_TYPE_UPDATE = 4
        const val DATA_CHANGE_TYPE_DELETE = 5
    }

    private var mDataSource: MutableList<PoMessageEntity> = ArrayList()
    private var mLoading = true
    private lateinit var mRecycleView: HookActionUpRecyclerView
    private var messageLongClick: OnMessageLongClick? = null
    private var userIconClick: OnUserIconClick? = null

    fun onMessageLongClick(messageLongClick: OnMessageLongClick?) {
        this.messageLongClick = messageLongClick
    }

    fun onUserIconClick(userIconClick: OnUserIconClick?) {
        this.userIconClick = userIconClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MessageBaseHolder.Factory.getInstance(parent, this, viewType)


    override fun getItemCount(): Int = mDataSource.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg: PoMessageEntity? = getItem(position)
        val baseHolder = holder as MessageBaseHolder
        when (getItemViewType(position)) {
            MSG_TYPE_HEADER_VIEW -> (baseHolder as MessageHeaderHolder).setLoadingStatus(mLoading)
            PoMessageEntity.MSG_TYPE_TEXT, PoMessageEntity.MSG_TYPE_IMAGE, PoMessageEntity.MSG_TYPE_VIDEO, PoMessageEntity.MSG_TYPE_CUSTOM_FACE, PoMessageEntity.MSG_TYPE_AUDIO, PoMessageEntity.MSG_TYPE_FILE -> {
            }
            else -> {
            }
        }
        baseHolder.layoutViews(msg, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecycleView = recyclerView as HookActionUpRecyclerView
        mRecycleView.setItemViewCacheSize(5)
    }

    fun showLoading() {
        if (mLoading) {
            return
        }
        mLoading = true
        notifyItemChanged(0)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is MessageContentHolder) {
            holder.msgContentFrame?.background = null
        }
    }

    fun notifyDataSourceChanged(type: Int, value: Int) {
        BackgroundTasks.getInstance().postDelayed(Runnable {
            mLoading = false
            if (type == DATA_CHANGE_TYPE_REFRESH) {
                notifyDataSetChanged()
                mRecycleView.scrollToEnd()
            } else if (type == DATA_CHANGE_TYPE_ADD_BACK) {
                notifyItemRangeInserted(mDataSource.size + 1, value)
                notifyDataSetChanged()
                mRecycleView.scrollToEnd()
            } else if (type == DATA_CHANGE_TYPE_UPDATE) {
                notifyItemChanged(value + 1)
            } else if (type == DATA_CHANGE_TYPE_LOAD || type == DATA_CHANGE_TYPE_ADD_FRONT) {
                //加载条目为数0，只更新动画
                if (value == 0) {
                    notifyItemChanged(0)
                } else {
                    //加载过程中有可能之前第一条与新加载的最后一条的时间间隔不超过5分钟，时间条目需去掉，所以这里的刷新要多一个条目
                    if (itemCount > value) {
                        notifyItemRangeInserted(0, value)
                    } else {
                        notifyItemRangeInserted(0, value)
                    }
                }
            } else if (type == DATA_CHANGE_TYPE_DELETE) {
                notifyItemRemoved(value + 1)
                notifyDataSetChanged()
            }
        }, 100)
    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return MSG_TYPE_HEADER_VIEW
        }
        val msg: PoMessageEntity? = getItem(position)
        return msg!!.msgType
    }

    fun setDataSource(provider: IChatProvider?) {
        if (provider == null) {
            mDataSource.clear()
        } else {
            mDataSource = provider.getDataSource()
            provider.setAdapter(this)
        }
        notifyDataSourceChanged(DATA_CHANGE_TYPE_REFRESH, itemCount)

    }

    fun getItem(position: Int): PoMessageEntity? {
        if (position == 0 || mDataSource.size == 0) {
            return null
        }
        return mDataSource[position - 1]
    }
}
typealias OnMessageLongClick = (view: View?, position: Int, messageInfo: PoMessageEntity?) -> Unit
typealias OnUserIconClick = (view: View?, position: Int, messageInfo: PoMessageEntity?) -> Unit