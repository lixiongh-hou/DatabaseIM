package com.example.base.utli

import org.greenrobot.eventbus.EventBus

/**
 * @author 李雄厚
 *
 * @features ***
 */
object EventBusUtil {

    /**
     * 注册EventBus
     * @param subscriber 订阅者对象
     */
    fun register(subscriber: Any?){
        if (!EventBus.getDefault().isRegistered(subscriber)){
            EventBus.getDefault().register(subscriber)
        }
    }

    /**
     * 取消注册EventBus
     * @param subscriber 订阅者对象
     */
    fun unregister(subscriber: Any?){
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    /**
     * 发布订阅事件
     *
     * @param event 事件对象
     */
    fun post(event: Any?) {
        EventBus.getDefault().post(event)
    }
}