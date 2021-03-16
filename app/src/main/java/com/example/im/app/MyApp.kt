package com.example.im.app

import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.example.base.base.BaseApp
import com.example.im.util.BackgroundTasks
import com.example.im.util.EmotionManager

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MyApp: BaseApp(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
        BackgroundTasks.initInstance()
        EmotionManager.loadFaceFiles()
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}