package com.example.im.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 *@author: 雄厚
 *Date: 2020/9/3
 *Time: 11:37
 */
object ViewStyleUtil {

    /**
     * 修改图片底色
     * @sample id 图片资源
     * @sample resId 颜色资源
     */
    fun getBackground(context: Context, @DrawableRes id: Int, resId: Int) : Drawable?{
        return ContextCompat.getDrawable(context, id)?.let {
            wrappedDrawable(
                    it,
                    ColorStateList.valueOf(ContextCompat.getColor(context, resId)))
        }
    }
    /**
     * 修改drawable 的颜色
     */
    private fun wrappedDrawable(drawable: Drawable, colors: ColorStateList): Drawable? {
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }

    /**
     * SwitchCompat 样式
     * @sample normal1 不知，有时间自己查阅资料
     * @sample selected 选中颜色
     * @sample normal 未选中颜色
     */
    fun thumbColorStateList(normal1: Int, selected: Int, normal: Int): ColorStateList? {
        //其中:-android.R.attr.state_enabled 和 android.R.attr.state_checked 的区别在于 “-” 号代表值里的true 和 false ,有“-”为false 没有则为true
        val colors = intArrayOf(normal1, selected, normal)
        val states = arrayOfNulls<IntArray>(3)
        states[0] = intArrayOf(-android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_checked)
        states[2] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 也是设置SwitchCompat轨道颜色,方法和上面一样所有目前用不到，要用改完public即可
     */
    private fun trackColorStateList(normal1: Int, selected: Int, normal: Int): ColorStateList? {
        //其中:-android.R.attr.state_enabled 和 android.R.attr.state_checked 的区别在于 “-” 号代表值里的true 和 false ,有“-”为false 没有则为true
        val colors = intArrayOf(selected, normal, normal1)
        val states = arrayOfNulls<IntArray>(3)
        states[0] = intArrayOf(-android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_checked)
        states[2] = intArrayOf()
        return ColorStateList(states, colors)
    }
}