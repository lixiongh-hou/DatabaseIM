package com.example.base.base

import com.example.base.const.Status

/**
 * @author 李雄厚
 *
 * @features API接口反应体
 */
class BaseResponse<out T>(val errorCode: Int, val errorMsg: String?, val data: T?){
    fun success():Boolean{
        return errorCode== Status.SUCCESS
    }
}