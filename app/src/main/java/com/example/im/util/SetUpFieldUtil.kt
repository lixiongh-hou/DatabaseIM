package com.example.im.util

import com.example.base.sp.SharedPref
import com.example.im.entity.SetUpField

/**
 * @author 李雄厚
 *
 * @features ***
 */
object SetUpFieldUtil {
   private var firstChatRefreshTime by SharedPref(Field.FIRST_CHAT_REFRESH_TIME, 300L)
   private var pullRefreshTime by SharedPref(Field.PULL_REFRESH_TIME, 300L)
   private var msgSuccessOrFailure by SharedPref(Field.MSG_SUCCESS_OR_FAILURE, true)
   private var localMsg by SharedPref(Field.LOCAL_MSG, true)
   private var deleteMessage by SharedPref(Field.DELETE_MESSAGE, true)
   private var revokeMessageTime by SharedPref(Field.REVOKE_MESSAGE_TIME, 5)
   private var revokeMessage by SharedPref(Field.REVOKE_MESSAGE, true)
   private var revokeMessageLocal by SharedPref(Field.REVOKE_MESSAGE_LOCAL, true)

    fun saveField(field: SetUpField){
        if (field.firstChatRefreshTime != null){
            firstChatRefreshTime = field.firstChatRefreshTime!!
        }
        if (field.pullRefreshTime != null){
            pullRefreshTime = field.pullRefreshTime!!
        }
        if (field.msgSuccessOrFailure != null){
            msgSuccessOrFailure = field.msgSuccessOrFailure!!
        }
        if (field.localMsg != null){
            localMsg = field.localMsg!!
        }
        if (field.deleteMessage != null){
            deleteMessage = field.deleteMessage!!
        }
        if (field.revokeMessageTime != null){
            revokeMessageTime = field.revokeMessageTime!!
        }
        if (field.revokeMessage != null){
            revokeMessage = field.revokeMessage!!
        }
        if (field.revokeMessageLocal != null){
            revokeMessageLocal = field.revokeMessageLocal!!
        }
    }

    fun getField(): SetUpField{
        val field = SetUpField()
        field.firstChatRefreshTime = firstChatRefreshTime
        field.pullRefreshTime = pullRefreshTime
        field.msgSuccessOrFailure = msgSuccessOrFailure
        field.localMsg = localMsg
        field.deleteMessage = deleteMessage
        field.revokeMessageTime = revokeMessageTime
        field.revokeMessage = revokeMessage
        field.revokeMessageLocal = revokeMessageLocal
        return field
    }

}