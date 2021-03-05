package com.example.im.home.model

import androidx.lifecycle.MutableLiveData
import com.example.base.base.BaseViewModel
import com.example.im.home.chat.ChatManagerKit
import com.example.im.home.chat.entity.ChatProvider
import com.example.im.home.chat.entity.PageResponse
import com.example.im.home.chat.entity.PoMessageEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MessageViewModel: BaseViewModel() {
    val loadMessage = MutableLiveData<ChatProvider>()
    val refreshMessage = MutableLiveData<PageResponse<PoMessageEntity>>()

    fun getMessage(page: String, id: String,  first: Boolean){
        ChatManagerKit.loadConversation(page, id, first, {
            refreshMessage.postValue(it)
        }, {
            loadMessage.postValue(it)
        })
    }
}