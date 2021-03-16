package com.example.im.home.chat.util

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.chat.entity.QueryEntry
import com.example.im.home.fragment.ChatFragment
import com.example.im.util.ImageUtil
import com.example.im.util.UserUtil
import java.io.File
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
            if (self) UserUtil.getMyHead() else info.iconUrl,
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
            if (self) UserUtil.getMyHead() else info.iconUrl,
            size[0],
            size[1],
            System.currentTimeMillis(),
            false,
            true
        )
    }

    fun buildVideoMessage(
        id: String,
        path: String,
        self: Boolean,
        info: QueryEntry
    ): PoMessageEntity? {
        val mmr = MediaMetadataRetriever()
        try {
            mmr.setDataSource(path)
            //缩略图
            val bitmap = mmr.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC)
            if (bitmap == null) {
                Log.e("测试", "buildVideoMessage() bitmap is null")
                return null
            }
            val imgWidth = bitmap.width
            val imgHeight = bitmap.height

            return PoMessageEntity(
                id,
                UUID.randomUUID().toString(),
                PoMessageEntity.MSG_TYPE_VIDEO,
                PoMessageEntity.MSG_STATUS_NORMAL,
                self,
                false,
                path,
                "[视频]",
                if (self) UserUtil.getMyHead() else info.iconUrl,
                imgWidth,
                imgHeight,
                System.currentTimeMillis(),
                false,
                true
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.e("测试", "MediaMetadataRetriever exception $ex")
        } finally {
            mmr.release()
        }
        return null
    }

}