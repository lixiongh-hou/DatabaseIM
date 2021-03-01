package com.example.base.utli

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 李雄厚
 *
 * @features 时间工具类
 */
object TimeUtil {
    /**
     * 1分钟
     */
    private const val minute = 60 * 1000.toLong()

    /**
     * 1小时
     */
    private const val hour = 60 * minute

    /**
     * 1天
     */
    private const val day = 24 * hour

    /**
     * 月
     */
    private const val month = 31 * day

    /**
     * 年
     */
    private const val year = 12 * month


    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun getTimeFormatText(time: String?): String {
        if (time == null) {
            return ""
        }
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = formatter.parse(time) ?: return ""
        val diff = Date().time - date.time
        val r: Long
        if (diff > year){
            return time
        }
        if (diff > month){
            return time
        }

        if (diff > day){
            r = (diff / day)
            return "${r}天前"
        }

        if (diff > hour){
            r = (diff / hour)
            return "${r}小时前"
        }

        if (diff > minute){
            r = (diff / minute)
            return "${r}分钟前"
        }
        return "刚刚"
    }
}