package com.example.base.base


/**
 * @author 李雄厚
 *
 * @features API接口反应体
 */
class BaseResponse1<out T>(val code: Int, val msg: String?, val result: T?){
    fun success():Boolean{
        return code== 200
    }
}