package com.example.im.home.chat.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Entity(tableName = "message")
data class PoMessageEntity(
    /**
     * 对方ID
     */
    val fromUser: String,
    /**
     * 消息id
     */
    val msgId: String,
    /**
     * 消息类型
     */
    var msgType: Int,
    /**
     * 消息发送状态
     */
    var status: Int = MSG_STATUS_NORMAL,
    /**
     * 是否是自己发送
     */
    val self: Boolean,
    /**
     * 消息是否已读
     */
    val read: Boolean,
    /**
     * 获取多媒体路径
     */
    val dataPath: String,
    /**
     * 文字说明
     */
    var extra: String,
    /**
     * 用户头像
     */
    val faceUrl: String,
    /**
     * 图片或视频宽
     */
    val imgWidth: Int,
    /**
     * 图片或视频宽
     */
    val imgHeight: Int,
    /**
     * 消息发送时间
     */
    val msgTime: Long,
    /**
     * 语音消息是否已读
     */
    val peerRead: Boolean,
    /**
     * 是否是聊天消息,除了提示消息就是聊天消息
     */
    val chatMessage: Boolean
) {
    /**
     * 主键ID
     */
    @PrimaryKey(autoGenerate = true)
    var keyId: Int = 0

    companion object {
        const val MSG_TYPE_MIME = 0x1

        /**
         * 文本类型消息
         */
        const val MSG_TYPE_TEXT = 0x00

        /**
         * 图片类型消息
         */
        const val MSG_TYPE_IMAGE = 0x20

        /**
         * 语音类型消息
         */
        const val MSG_TYPE_AUDIO = 0x30

        /**
         * 视频类型消息
         */
        const val MSG_TYPE_VIDEO = 0x40

        /**
         * 文件类型消息
         */
        const val MSG_TYPE_FILE = 0x50

        /**
         * 位置类型消息
         */
        const val MSG_TYPE_LOCATION = 0x60

        /**
         * 自定义图片类型消息
         */
        const val MSG_TYPE_CUSTOM_FACE = 0x70

        /**
         * 自定义消息
         */
        const val MSG_TYPE_CUSTOM = 0x80

        /**
         * 提示类信息
         */
        const val MSG_TYPE_TIPS = 0x100

        /**
         * 群创建提示消息
         */
        const val MSG_TYPE_GROUP_CREATE = 0x101

        /**
         * 群解散提示消息
         */
        const val MSG_TYPE_GROUP_DELETE = 0x102

        /**
         * 群成员加入提示消息
         */
        const val MSG_TYPE_GROUP_JOIN = 0x103

        /**
         * 群成员退群提示消息
         */
        const val MSG_TYPE_GROUP_QUITE = 0x104

        /**
         * 群成员被踢出群提示消息
         */
        const val MSG_TYPE_GROUP_KICK = 0x105

        /**
         * 群名称修改提示消息
         */
        const val MSG_TYPE_GROUP_MODIFY_NAME = 0x106

        /**
         * 群通知更新提示消息
         */
        const val MSG_TYPE_GROUP_MODIFY_NOTICE = 0x107

        /**
         * 群音视频呼叫提示消息
         */
        const val MSG_TYPE_GROUP_AV_CALL_NOTICE = 0x108

        /**
         * 消息未读状态
         */
        const val MSG_STATUS_READ = 0x111

        /**
         * 消息删除状态
         */
        const val MSG_STATUS_DELETE = 0x112

        /**
         * 消息撤回状态
         */
        const val MSG_STATUS_REVOKE = 0x113

        /**
         * 消息正常状态
         */
        const val MSG_STATUS_NORMAL = 0

        /**
         * 消息发送中状态
         */
        const val MSG_STATUS_SENDING = 1

        /**
         * 消息发送成功状态
         */
        const val MSG_STATUS_SEND_SUCCESS = 2

        /**
         * 消息发送失败状态
         */
        const val MSG_STATUS_SEND_FAIL = 3

        /**
         * 消息内容下载中状态
         */
        const val MSG_STATUS_DOWNLOADING = 4

        /**
         * 消息内容未下载状态
         */
        const val MSG_STATUS_UN_DOWNLOAD = 5

        /**
         * 消息内容已下载状态
         */
        const val MSG_STATUS_DOWNLOADED = 6

    }
}