package com.example.base.utli

import androidx.recyclerview.widget.RecyclerView

/**
 * @author 李雄厚
 *
 * @features 防止Rv嵌套滑动布局问题
 */
object RvUtil {

    /**
     * 解决嵌套问题
     */
    fun solveNestQuestion(rv: RecyclerView) {
        // 解决数据加载不全的问题
        rv.isNestedScrollingEnabled = false
        rv.setHasFixedSize(true)
        // 解决数据加载完成后，没有停留在顶部的问题
        rv.isFocusable = false
    }
}