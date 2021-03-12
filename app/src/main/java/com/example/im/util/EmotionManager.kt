package com.example.im.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.util.LruCache
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.base.BaseApp
import com.example.base.utli.DensityUtil
import com.example.im.R
import com.example.im.adapter.EmotionAdapter
import com.example.im.entity.EmotionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern

/**
 * @author 李雄厚
 *
 * @features ***
 */
object EmotionManager {
    private val emotion: MutableList<EmotionEntity> = ArrayList()
    private val drawableWidth: Int = DensityUtil.dp2px(32F)
    private val emotionFilters: Array<String> = BaseApp.instance.resources.getStringArray(R.array.emotion_filter)
    private val drawableCache: LruCache<String, Bitmap> = LruCache(1024)

    fun getEmotionList(): MutableList<EmotionEntity> {
        return emotion
    }

    fun loadFaceFiles() {
        GlobalScope.launch(Dispatchers.IO) {
            for (emotionFilter in emotionFilters) {
                loadAssetBitmap(emotionFilter, "face/$emotionFilter@2x.png")
            }
        }
    }

    private fun loadAssetBitmap(filter: String, assetPath: String): EmotionEntity? {
        var stream: InputStream? = null
        try {
            val entity = EmotionEntity()
            val resources = BaseApp.instance.resources
            val options = BitmapFactory.Options()
            options.inDensity = DisplayMetrics.DENSITY_XXHIGH
            options.inScreenDensity = resources.displayMetrics.densityDpi
            options.inTargetDensity = resources.displayMetrics.densityDpi
            BaseApp.instance.assets.list("")
            stream = BaseApp.instance.assets.open(assetPath)
            val bitmap = BitmapFactory.decodeStream(stream, Rect(0, 0, drawableWidth, drawableWidth), options)
            if (bitmap != null) {
                drawableCache.put(filter, bitmap)
                entity.icon = bitmap
                entity.filter = filter
                emotion.add(entity)
            }
            return entity
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun isFaceChar(faceChar: String?): Boolean {
        return drawableCache.get(faceChar) != null
    }

    fun handlerEmotionText(comment: TextView, content: String, typing: Boolean) {
        val sb = SpannableStringBuilder(content)
        val regex = "\\[(\\S+?)\\]"
        val p = Pattern.compile(regex)
        val m = p.matcher(content)
        var imageFound = false
        while (m.find()) {
            val emotionName = m.group()
            val bitmap = drawableCache.get(emotionName)
            if (bitmap != null) {
                imageFound = true
                sb.setSpan(ImageSpan(BaseApp.instance, bitmap),
                        m.start(), m.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
        // 如果没有发现表情图片，并且当前是输入状态，不再重设输入框
        if (!imageFound && typing) {
            return
        }
        val selection = comment.selectionStart
        comment.text = sb
        if (comment is EditText) {
            comment.setSelection(selection)
        }
    }

    fun setEmotion(
        context: Context,
        rv: RecyclerView,
        delete: ImageView,
        editText: EditText,
        mFaceList: MutableList<EmotionEntity>
    ) {
        rv.layoutManager = GridLayoutManager(context, 7)
        val adapter = EmotionAdapter()
        rv.adapter = adapter
        adapter.refreshData(mFaceList)
        adapter.clickEvent = { _, _, position ->
            val index = editText.selectionStart
            val editable = editText.text
            editable.insert(index, adapter.data[position].filter)
            handlerEmotionText(editText, editable.toString(), true)
        }
        delete.setOnClickListener {
            val index: Int = editText.selectionStart
            val editable: Editable = editText.text
            var isFace = false
            if (index <= 0) {
                return@setOnClickListener
            }
            if (editable[index - 1] == ']') {
                for (i in index - 2 downTo 0) {
                    if (editable[i] == '[') {
                        val faceChar = editable.subSequence(i, index).toString()
                        if (isFaceChar(faceChar)) {
                            editable.delete(i, index)
                            isFace = true
                        }
                        break
                    }
                }
            }
            if (!isFace) {
                editable.delete(index - 1, index)
            }
        }

    }
}