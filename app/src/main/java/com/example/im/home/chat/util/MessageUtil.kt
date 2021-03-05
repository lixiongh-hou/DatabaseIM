package com.example.im.home.chat.util

import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.fragment.ChatFragment
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
object MessageUtil {

    fun sendMessage(id: String, extra: String, self: Boolean, info: ConversationEntity): PoMessageEntity {
        return PoMessageEntity(
            id,
            UUID.randomUUID().toString(),
            PoMessageEntity.MSG_TYPE_TEXT,
            PoMessageEntity.MSG_STATUS_NORMAL,
            self,
            false,
            "",
            extra,
            if (self) ChatFragment.MY_URL else info.iconUrl,
            0,
            0,
            System.currentTimeMillis(),
            false,
            true
        )

    }
}