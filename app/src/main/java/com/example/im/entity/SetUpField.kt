package com.example.im.entity

/**
 * @author 李雄厚
 *
 * @features ***
 */
class SetUpField {

    /**
     * 首次进入聊天刷新时间,单位毫秒
     */
    var firstChatRefreshTime: Long? = null

    /**
     * 下拉刷新时间,单位毫秒
     */
    var pullRefreshTime: Long? = null

    /**
     * 消息成功-失败
     */
    var msgSuccessOrFailure: Boolean? = null

    /**
     * 发送消息保存本地，前提条件是这条会话已经保存到本地,否正这个字段没用
     */
    var localMsg: Boolean? = null

    /**
     * 删除消息是否删除本地数据库
     */
    var deleteMessage: Boolean? = null

    /**
     * 撤回销售时间,单位分钟
     */
    var revokeMessageTime: Int? = null

    /**
     * 是否限制撤回消息
     */
    var revokeMessage: Boolean? = null

    /**
     * 撤回消息是否保持本地
     */
    var revokeMessageLocal: Boolean? = null
}