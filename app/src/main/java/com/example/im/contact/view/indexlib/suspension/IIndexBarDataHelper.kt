package com.example.im.contact.view.indexlib.suspension

import com.example.im.contact.view.indexlib.indexBar.BaseIndexPinyinBean

/**
 * @author 李雄厚
 *
 * @features ***
 */
interface IIndexBarDataHelper {

    /**汉语-》拼音*/
    fun convert(data: List<BaseIndexPinyinBean>): IIndexBarDataHelper

    /**拼音->tag*/
    fun fillIndexTag(data: List<BaseIndexPinyinBean>): IIndexBarDataHelper


    /**对源数据进行排序（RecyclerView）*/
    fun sortSourceDates(dates: List<BaseIndexPinyinBean>): IIndexBarDataHelper

    /**对IndexBar的数据源进行排序(右侧栏),在 sortSourceDates 方法后调用*/
    fun getSortedIndexDates(
        sourceDates: MutableList<out BaseIndexPinyinBean>,
        dates: MutableList<String>
    ): IIndexBarDataHelper

}