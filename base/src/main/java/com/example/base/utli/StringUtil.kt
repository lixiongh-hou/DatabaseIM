package com.example.base.utli

import android.text.TextUtils

/**
 * @author 李雄厚
 *
 * @features ***
 */
object StringUtil{

    fun formatStr(format: String, str: Any): String {
        if (TextUtils.isEmpty(format)) {
            return ""
        }
        when (str) {
            is String -> {
                return if (TextUtils.isEmpty(str)) {
                    String.format(format, "")
                } else {
                    String.format(format, str as String?)
                }
            }
            is Int -> {
                return String.format(format, str)
            }
            is Float -> {
                return String.format(format, str)
            }
            else -> return ""
        }
    }

}