package com.example.base.utli

import android.content.res.Resources
import android.os.Build

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ScreenUtil {

    /**
     * 屏幕宽度
     */
    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }


    /**
     * 屏幕高度
     */
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    /**
     * 获取状态栏高度
     */
    private fun getStatusBarHeight(): Int {
        val barHeight: Int
        val resId = Resources.getSystem()
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            barHeight = Resources.getSystem().getDimensionPixelSize(resId)
        } else {
            barHeight = DensityUtil.dp2px(24F)
        }
        return barHeight
    }

    /**
     * 获取状态栏高度(实际使用)
     */
    fun getRealStatusBarHeight(): Int {
        var barHeight = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            barHeight = getStatusBarHeight()
        }
        return barHeight
    }
}