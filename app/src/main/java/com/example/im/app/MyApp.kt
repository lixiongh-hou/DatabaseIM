package com.example.im.app

import com.example.base.base.BaseApp
import com.example.im.util.BackgroundTasks
import com.example.im.util.EmotionManager

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MyApp: BaseApp() {

    override fun onCreate() {
        super.onCreate()
        BackgroundTasks.initInstance()
        EmotionManager.loadFaceFiles()
    }
}