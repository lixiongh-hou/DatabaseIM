package com.example.base.utli

import android.text.InputFilter.LengthFilter
import android.text.Spanned

/**
 * @author 李雄厚
 *
 * @features ***
 */
class DecimalsLengthFilter(max: Int, digits: Int) : LengthFilter(max) {
    /** 输入框小数的位数  */
    private val decimalDigits = digits

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // 删除等特殊字符，直接返回


        // 删除等特殊字符，直接返回
        if ("" == source.toString()) {
            return null
        }

        if (decimalDigits == 0) { // 整数
            return ""
        }

        val dValue = dest.toString()
        val splitArray = dValue.split("\\.".toRegex()).toTypedArray()

        if (splitArray.size > 1) {
            val dotValue = splitArray[1]
            val diff = dotValue.length + 1 - decimalDigits
            if (diff > 0) {
                return source!!.subSequence(start, end - diff)
            }
        }

        return super.filter(source, start, end, dest, dstart, dend)
    }
}