package com.example.im.home.chat.util

import android.net.Uri
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.chat.entity.QueryEntry
import com.example.im.home.fragment.ChatFragment
import com.example.im.util.ImageUtil
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
object MessageUtil {

    fun sendMessage(id: String, extra: String, self: Boolean, info: QueryEntry): PoMessageEntity {
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

    fun buildImageMessage(
        uri: Uri?,
        id: String,
        path: String,
        self: Boolean,
        info: QueryEntry
    ): PoMessageEntity {
        val size: IntArray = ImageUtil.getImageSize(uri)
        return PoMessageEntity(
            id,
            UUID.randomUUID().toString(),
            PoMessageEntity.MSG_TYPE_IMAGE,
            PoMessageEntity.MSG_STATUS_NORMAL,
            self,
            false,
            path,
            "[图片]",
            if (self) ChatFragment.MY_URL else info.iconUrl,
            size[0],
            size[1],
            System.currentTimeMillis(),
            false,
            true
        )
    }
}