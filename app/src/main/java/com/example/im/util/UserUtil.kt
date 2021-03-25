package com.example.im.util

import com.example.base.sp.SharedPref

const val MY_URL =
    "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=840492371,1920645159&fm=26&gp=0.jpg"

/**
 * @author 李雄厚
 *
 * @features ***
 */
object UserUtil {
    private var head by SharedPref(Field.HEAD, MY_URL)
    private var name by SharedPref(Field.NAME, "李雄厚")

    @JvmStatic
    fun getMyHead(): String {
        return head
    }

    fun setMyHead(head: String) {
        this.head = head
    }

    @JvmStatic
    fun getMyName(): String {
        return name
    }

    @JvmStatic
    fun setMyName(name: String) {
        this.name = name
    }

}