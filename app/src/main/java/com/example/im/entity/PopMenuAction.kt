package com.example.im.entity

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

/**
 * @author 李雄厚
 *
 * @features ***
 */
class PopMenuAction {

    var actionName: String = ""
    var icon: Bitmap? = null
    @DrawableRes
    var iconResId: Int = -1
    private var actionClickListener: PopActionClickListener? = null

    fun setActionClickListener(actionClickListener: PopActionClickListener?) {
        this.actionClickListener = actionClickListener
    }
    fun getActionClickListener():PopActionClickListener?{
        return actionClickListener
    }
}

typealias PopActionClickListener = (position: Int, data: Any) -> Unit