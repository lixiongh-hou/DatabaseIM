package com.example.im.home.chat.entity

/**
 * @author 李雄厚
 *
 * @features ***
 */
data class PageResponse<T>(
        /**
         * 当前页数
         */
        val pageNo: Int,
        /**
         * 当前数据大小
         */
        val count: Int,
        /**
         * 总页数
         */
        val totalPage: Int,
        /**
         * 总数据大小
         */
        val totalCount: Int,
        /**
         * 一页多少数据
         */
        val pageSize: Int,
        /**
         * 数据
         */
        val lists: MutableList<T>

)