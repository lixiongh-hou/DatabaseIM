package com.example.im.util

import android.os.Handler

/**
 * @author 李雄厚
 *
 * @features ***
 */
class BackgroundTasks {
    companion object{
        private lateinit var instance: BackgroundTasks
        private val mHandler = Handler()
        fun getInstance(): BackgroundTasks {
            return instance
        }
        // 需要在主线程中初始化
        fun initInstance() {
            instance = BackgroundTasks()
        }
    }

    fun runOnUiThread(runnable: Runnable) {
        mHandler.post(runnable)
    }

    fun postDelayed(r: Runnable, delayMillis: Long): Boolean {
        return mHandler.postDelayed(r, delayMillis)
    }

    fun getHandler(): Handler {
        return mHandler
    }

}