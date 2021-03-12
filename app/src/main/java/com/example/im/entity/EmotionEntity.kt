package com.example.im.entity

import android.graphics.Bitmap
import com.example.base.utli.DensityUtil
import java.io.Serializable

/**
 * @author 李雄厚
 *
 * @features ***
 */
class EmotionEntity : Serializable {
    var defaultSize: Int = DensityUtil.dp2px(32F)
    var desc: String? = ""
    var filter: String? = ""
    var icon: Bitmap? = null
    val width: Int = defaultSize
    val height: Int = defaultSize
}