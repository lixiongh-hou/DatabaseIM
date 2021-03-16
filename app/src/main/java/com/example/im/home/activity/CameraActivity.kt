package com.example.im.home.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.base.base.BaseActivity
import com.example.im.R
import com.example.im.databinding.ActivityCameraBinding
import com.hbzhou.open.flowcamera.CaptureImageButton.Companion.BUTTON_STATE_BOTH
import com.hbzhou.open.flowcamera.CaptureImageButton.Companion.BUTTON_STATE_ONLY_RECORDER
import com.hbzhou.open.flowcamera.listener.FlowCameraListener
import com.otaliastudios.cameraview.controls.Hdr
import com.otaliastudios.cameraview.controls.WhiteBalance
import java.io.File

/**
 * @author 李雄厚
 *
 * @features ***
 */
class CameraActivity : BaseActivity<ActivityCameraBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //去除状态栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun initLayout(): Int = R.layout.activity_camera

    override fun initView() {
        // 绑定生命周期 您就不用关心Camera的开启和关闭了 不绑定无法预览
        mBinding.flowCamera.setBindToLifecycle(this)
        // 设置白平衡模式
        mBinding.flowCamera.setWhiteBalance(WhiteBalance.AUTO)
        // 设置白平衡模式
        mBinding.flowCamera.setCaptureMode(BUTTON_STATE_BOTH)
        // 开启HDR
        mBinding.flowCamera.setHdrEnable(Hdr.ON)
        // 设置最大可拍摄小视频时长
        mBinding.flowCamera.setRecordVideoMaxTime(10)
        // 设置拍照或拍视频回调监听
        mBinding.flowCamera.setFlowCameraListener(object : FlowCameraListener {
            // 录制完成视频文件返回
            override fun recordSuccess(file: File) {
                Log.e("测试", "视频" + file.absolutePath)
                val intent = Intent()
                intent.putExtra(Const.PATH, file.absolutePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            // 操作拍照或录视频出错
            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {

            }

            // 拍照返回
            override fun captureSuccess(file: File) {
                Log.e("测试", "拍照" + file.absolutePath)
                val intent = Intent()
                intent.putExtra(Const.PATH, file.absolutePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
                finish()
            }
        })
        //左边按钮点击事件
        mBinding.flowCamera.setLeftClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }
}