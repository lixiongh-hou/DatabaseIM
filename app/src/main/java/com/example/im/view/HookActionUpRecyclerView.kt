package com.example.im.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.utli.DensityUtil
import com.example.im.home.chat.adapter.MessageListAdapter
import kotlin.math.abs

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HookActionUpRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var startScroll = false
    private var curDownX = 0f
    private var curDownY = 0f
    private val scrollMax: Float = DensityUtil.dp2px(8F).toFloat()
    private var loadMore: (() -> Unit)? = null
    private var emptySpace: (() -> Unit)? = null
    /**
     * 是否有更多数据 默认没有
     */
    private var hasMoreData = false

    init {
        suppressLayout(false)
        setItemViewCacheSize(0)
        setHasFixedSize(true)
        isFocusableInTouchMode = false
        val linearLayoutManager: LinearLayoutManager = CustomLinearLayoutManager(getContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        layoutManager = linearLayoutManager

    }


    /**
     * 具体规则查 {@link com.effective.android.panel.view.content.ContentContainerImpl}
     *
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val curX = e.x
        val curY = e.y
        if (e.action == MotionEvent.ACTION_DOWN) {
            startScroll = false
            curDownX = curX
            curDownY = curY
        }

        if (e.action == MotionEvent.ACTION_MOVE) {
            startScroll = abs(curX - curDownX) > scrollMax || abs(curY - curDownY) > scrollMax
        }
        if (e.action == MotionEvent.ACTION_UP && !startScroll) {
            return false
        }

        return super.onTouchEvent(e)
    }

    override fun onScrollStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == SCROLL_STATE_IDLE) {
            val layoutManager = layoutManager as LinearLayoutManager
            val firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (firstPosition == 0 && (lastPosition - firstPosition + 1) < adapter!!.itemCount) {
                if (hasMoreData) {
                    Log.e("测试", "加载更多")
                    if (adapter is MessageListAdapter) {
                        (adapter as MessageListAdapter).showLoading()
                    }
                    loadMore?.invoke()
                }
            }

        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_UP) {
            val child = findChildViewUnder(e.x, e.y)
            if (child == null) {
                emptySpace?.invoke()
            } else if (child is ViewGroup) {
                val count = child.childCount
                val x = e.rawX
                val y = e.rawY
                var touchChild: View? = null
                for (i in count - 1 downTo 0) {
                    val innerChild = child.getChildAt(i)
                    val position = IntArray(2)
                    innerChild.getLocationOnScreen(position)
                    if (x >= position[0] && x <= position[0] + innerChild.measuredWidth && y >= position[1] && y <= position[1] + innerChild.measuredHeight) {
                        touchChild = innerChild
                        break
                    }
                }
                if (touchChild == null) {
                    emptySpace?.invoke()
                }
            }
        }

        return super.onInterceptTouchEvent(e)
    }

    fun scrollToEnd() {
        if (adapter != null) {
            scrollToPosition(adapter!!.itemCount - 1)
        }
    }

    fun setOnLoadMoreHandler(loadMore: () -> Unit) {
        this.loadMore = loadMore
    }

    fun setOnEmptySpaceClickListener(emptySpace: () -> Unit){
        this.emptySpace = emptySpace
    }

    fun finishRefresh(hasMoreData: Boolean){
        this.hasMoreData = hasMoreData
    }
}