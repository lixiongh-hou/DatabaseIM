package com.example.im.home.chat.util

import android.util.Log
import com.example.im.contact.entity.PoContactEntity
import com.example.im.db.CommonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ContactKit {

    fun saveContact(entity: PoContactEntity, success: (PoContactEntity) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (entity.saveLocal) {
                    CommonDatabase.database.contactDao().save(entity)
                }
                success.invoke(entity)
            } catch (e: Exception) {
                Log.e("测试", "通讯录保存失败$e")
            }
        }
    }

    fun queryAllContact(success: (MutableList<PoContactEntity>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                success.invoke(CommonDatabase.database.contactDao().queryAll())
            } catch (e: Exception) {
                Log.e("测试", "通讯录保存失败$e")
            }
        }
    }
}