package com.example.im.contact.entity

import com.example.im.contact.view.indexlib.indexBar.BaseIndexPinyinBean

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ContactEntity : BaseIndexPinyinBean {

    companion object{
        const val INDEX_STRING_TOP = "↑"
    }

    /**
     * 用户ID
     */
    private var id: String = ""
    /**是否是最上面的 不需要被转化成拼音的*/
    private var isTop: Boolean = true
    /**是否需要悬停*/
    private var isSuspension: Boolean = true
    /**是否被选中*/
    private var isSelected: Boolean = false
    /**昵称*/
    private var nickname: String = ""
    /**头像*/
    private var avatar: String = ""
    /**
     * 是否保持本地
     */
    var saveLocal: Boolean = true

    constructor()

    constructor(nickname: String) {
        this.nickname = nickname
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String): ContactEntity {
        this.id = id
        return this
    }


    fun isTop(): Boolean {
        return isTop
    }

    fun setTop(top: Boolean): ContactEntity {
        isTop = top
        return this
    }
    fun isSuspension(): Boolean {
        return isSuspension
    }

    fun setSuspension(suspension: Boolean): ContactEntity {
        isSuspension = suspension
        return this
    }

    override fun getTarget(): String {
        if (nickname.isNotEmpty()){
            return nickname
        }
        return id

    }

    override fun isShowSuspension(): Boolean {
        return isSuspension
    }

    override fun isNeedToPinyin(): Boolean {
        return isTop
    }


    fun isSelected(): Boolean {
        return isSelected
    }

    fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    fun getNickname(): String {
        return nickname
    }

    fun setNickname(nickname: String) {
        this.nickname = nickname
    }


    fun getAvatar(): String {
        return avatar
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
    }

}