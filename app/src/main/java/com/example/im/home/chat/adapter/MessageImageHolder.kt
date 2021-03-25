package com.example.im.home.chat.adapter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.base.base.BaseApp
import com.example.im.R
import com.example.im.home.chat.entity.PoMessageEntity


private const val DEFAULT_MAX_SIZE = 540
private const val DEFAULT_RADIUS = 10

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MessageImageHolder(itemView: View) : MessageContentHolder(itemView) {

    private var contentImage: ImageView? = null
    private var videoPlayBtn: ImageView? = null
    private var videoDurationText: TextView? = null

    private val normalOptions: RequestOptions = RequestOptions.bitmapTransform(RoundedCorners(10))
        .error(R.drawable.default_head)
        .placeholder(R.drawable.default_head)
        .dontAnimate()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    override fun layoutVariableViews(msg: PoMessageEntity?, position: Int) {
        msgContentFrame?.background = null
        when (msg?.msgType) {
            PoMessageEntity.MSG_TYPE_IMAGE, PoMessageEntity.MSG_TYPE_IMAGE + 1 -> {
                performImage(msg, position)
            }
            PoMessageEntity.MSG_TYPE_VIDEO, PoMessageEntity.MSG_TYPE_VIDEO + 1 -> {
                performVideo(msg)
            }
        }
    }

    private fun resetParentLayout() {
        (contentImage!!.parent.parent as FrameLayout).setPadding(17, 0, 13, 0)
    }

    private fun performImage(msg: PoMessageEntity, position: Int) {
        contentImage?.layoutParams = getImageParams(contentImage!!.layoutParams, msg)
        resetParentLayout()
        videoPlayBtn?.visibility = View.GONE
        videoDurationText?.visibility = View.GONE

            Glide.with(BaseApp.instance).load(msg.dataPath) //图片地址
                .apply(normalOptions)
                .into(contentImage!!)
        // TODO 这个框架会在滑动到图片时候出现卡顿
//        contentImage?.load(getMediaUriFromPath(BaseApp.instance, msg.dataPath)) {
//            placeholder(R.drawable.default_head)
//            error(R.drawable.default_head)
//            transformations(RoundedCornersTransformation(8F))
//        }

        contentImage?.setOnClickListener {

        }
        contentImage?.setOnLongClickListener {
            messageLongClick?.invoke(it, position, msg)
            true
        }
    }

    override fun getVariableLayout(): Int = R.layout.message_adapter_content_image

    override fun initVariableViews() {
        contentImage = rootView.findViewById(R.id.content_image_iv)
        videoPlayBtn = rootView.findViewById(R.id.video_play_btn)
        videoDurationText = rootView.findViewById(R.id.video_duration_tv)
    }

    private fun getImageParams(
        params: ViewGroup.LayoutParams,
        msg: PoMessageEntity
    ): ViewGroup.LayoutParams? {
        if (msg.imgWidth == 0 || msg.imgHeight == 0) {
            return params
        }
        if (msg.imgWidth > msg.imgHeight) {
            params.width = DEFAULT_MAX_SIZE
            params.height = DEFAULT_MAX_SIZE * msg.imgHeight / msg.imgWidth
        } else {
            params.width = DEFAULT_MAX_SIZE * msg.imgWidth / msg.imgHeight
            params.height = DEFAULT_MAX_SIZE
        }
        return params
    }

    companion object {
        fun getMediaUriFromPath(context: Context, path: String): Uri? {
            val mediaUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor? = context.contentResolver.query(
                mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                arrayOf(path.substring(path.lastIndexOf("/") + 1)),
                null
            )
            var uri: Uri? = null
            if (cursor!!.moveToFirst()) {
                uri = ContentUris.withAppendedId(
                    mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                )
            }
            cursor.close()
            return uri
        }
    }


    private fun performVideo(msg: PoMessageEntity) {
        contentImage?.layoutParams = getImageParams(contentImage!!.layoutParams, msg)
        resetParentLayout()

        videoPlayBtn?.visibility = View.VISIBLE
        videoDurationText?.visibility = View.VISIBLE

        Glide.with(BaseApp.instance).load(msg.dataPath) //图片地址
            .apply(normalOptions)
            .into(contentImage!!)
        // TODO 这个框架会在滑动到图片时候出现卡顿
//        contentImage?.load(firstBitmap(msg.dataPath)) {
//            placeholder(R.drawable.default_head)
//            error(R.drawable.default_head)
//            transformations(RoundedCornersTransformation(8F))
//        }
        var durations = "00:" + duration(msg.dataPath) / 1000
        if (duration(msg.dataPath) < 10) {
            durations = "00:0" + duration(msg.dataPath)
        }
        videoDurationText?.text = durations

        contentImage?.setOnClickListener {

        }

        contentImage?.setOnLongClickListener {
            messageLongClick?.invoke(it, position, msg)
            true
        }
    }

    private fun firstBitmap(videoPath: String): Bitmap? {
        val media = MediaMetadataRetriever()
        // videoPath 本地视频的路径
        media.setDataSource(videoPath)
        return media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
    }

    private fun duration(videoPath: String): Long {
        val media = MediaMetadataRetriever()
        // videoPath 本地视频的路径
        media.setDataSource(videoPath)
        //时长(毫秒)
        val sDuration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return sDuration!!.toLong() / 1000
    }
}

