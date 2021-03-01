package com.example.base.base

import androidx.lifecycle.ViewModel
import com.example.base.bridge.EventLiveData

/**
 * @author 李雄厚
 *
 * @features ViewModel基类
 */
open class BaseViewModel : ViewModel() {
    val toastLiveData = EventLiveData<String>()
    val errorLiveData = EventLiveData<String>()
}