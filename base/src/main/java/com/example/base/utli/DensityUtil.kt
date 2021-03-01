package com.example.base.utli

import android.content.res.Resources

/**
 * @author 李雄厚
 *
 * @features ***
 */
object DensityUtil {

    /**
     * 获取密度
     */
    private fun getDensity(): Float {
        return Resources.getSystem().displayMetrics.density
    }

    /**
     * dip --> px
     */
    fun dp2px(dp: Float): Int {
        return (dp * getDensity() + 0.5f).toInt()
    }

    /**
     * px --> dp
     */
    fun px2dp(px: Float): Int {
        return (px / getDensity() + 0.5f).toInt()
    }


    /**
     * sp --> px
     */
    fun sp2px(sp: Float): Int {
        val sd =
            Resources.getSystem().displayMetrics.scaledDensity
        return (sp * sd + 0.5f).toInt()
    }

    /**
     * px --> sp
     */
    fun px2sp(px: Float): Int {
        val sd =
            Resources.getSystem().displayMetrics.scaledDensity
        return (px / sd + 0.5f).toInt()
    }
}