package com.example.im.home.model

import androidx.lifecycle.MutableLiveData
import com.example.base.base.BaseViewModel
import com.example.im.home.conversation.ConversationManagerKit
import com.example.im.home.conversation.entity.ConversationProvider

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HomeViewModel : BaseViewModel() {

    val success = MutableLiveData<ConversationProvider>()

    fun loadConversation() {
        ConversationManagerKit.loadConversation {
            success.postValue(it)
        }

    }
}