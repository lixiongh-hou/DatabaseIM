package com.example.im.contact.view.indexlib.suspension

/**
 * @author 李雄厚
 *
 * @features ***
 */
interface ISuspensionInterface {
    /**是否需要显示悬停title*/
    fun isShowSuspension(): Boolean

    /**悬停的title*/
    fun getSuspensionTag(): String
}