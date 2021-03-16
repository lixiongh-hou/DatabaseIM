package com.example.im.contact.model

import androidx.lifecycle.MutableLiveData
import com.example.base.base.BaseViewModel
import com.example.im.contact.entity.PoContactEntity
import com.example.im.home.chat.util.ContactKit

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ContactViewModel : BaseViewModel() {

    val queryAllContact = MutableLiveData<MutableList<PoContactEntity>>()


    fun queryAllContact() {
        ContactKit.queryAllContact {
            queryAllContact.postValue(it)
        }
    }
}