package com.example.im.home.chat.adapter

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import coil.load
import coil.transform.RoundedCornersTransformation
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
    override fun layoutVariableViews(msg: PoMessageEntity?, position: Int) {
        msgContentFrame?.background = null
        when (msg?.msgType) {
            PoMessageEntity.MSG_TYPE_IMAGE, PoMessageEntity.MSG_TYPE_IMAGE + 1 -> {
                performImage(msg, position)
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
        contentImage?.load(getMediaUriFromPath(BaseApp.instance, msg.dataPath)) {
            placeholder(R.drawable.default_head)
            error(R.drawable.default_head)
            transformations(RoundedCornersTransformation(8F))
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

    private fun getMediaUriFromPath(context: Context, path: String): Uri? {
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

