package com.example.im.contact.view.indexlib.indexBar

import com.example.im.contact.view.indexlib.suspension.ISuspensionInterface
import java.io.Serializable

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class BaseIndexBean: ISuspensionInterface, Serializable {
    /**所属的分类（名字的汉语拼音首字母）*/
    private var baseIndexTag: String? = null

    fun getBaseIndexTag(): String {
        return baseIndexTag!!
    }

    fun setBaseIndexTag(baseIndexTag: String): BaseIndexBean {
        this.baseIndexTag = baseIndexTag
        return this
    }

    /**
     * 获取拼音的首字母
     */
    override fun getSuspensionTag(): String {
        return baseIndexTag!!
    }

    /**
     * 是否需要悬停
     */
    override fun isShowSuspension(): Boolean {
        return false
    }
}