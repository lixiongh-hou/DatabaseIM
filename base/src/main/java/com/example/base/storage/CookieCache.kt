package com.example.base.storage

import com.example.base.const.Field
import com.example.base.sp.SharedPref

/**
 * @author 李雄厚
 *
 * @features 持久化存储的登录状态
 */
object CookieCache {
    private var Cookie by SharedPref(Field.COOKIE, "")

    @JvmStatic
    fun get(): String{
        return Cookie
    }

    fun set(Cookie: String){
        this.Cookie = Cookie
    }

    fun clear(){
        val prefs: SharedPref<String> = SharedPref()
        prefs.remove(Field.COOKIE)
    }
}