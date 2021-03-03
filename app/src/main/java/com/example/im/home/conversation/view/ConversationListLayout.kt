package com.example.im.home.conversation.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.adapter.OnItemClickListener
import com.example.im.home.conversation.adapter.OnItemLongClickListener
import com.example.im.view.CustomLinearLayoutManager

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ConversationListLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), IConversationListLayout {

    private lateinit var mAdapter: ConversationListAdapter

    init {
        suppressLayout(false)
        setItemViewCacheSize(0)
        setHasFixedSize(true)
        isFocusableInTouchMode = false
        val linearLayoutManager = CustomLinearLayoutManager(getContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager = linearLayoutManager
    }

    override fun setOnItemClickListener(listener: OnItemClickListener) {
        mAdapter.setOnItemClickListener(listener)
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mAdapter.setOnItemLongClickListener(listener)
    }

    override fun getAdapter(): ConversationListAdapter  = mAdapter

    override fun setAdapter(adapter: IConversationAdapter) {
        super.setAdapter(adapter)
        this.mAdapter = adapter as ConversationListAdapter
    }
}