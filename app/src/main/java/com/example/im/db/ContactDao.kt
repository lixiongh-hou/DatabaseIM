package com.example.im.db

import androidx.room.Dao
import androidx.room.Insert
import com.example.im.contact.entity.PoContactEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
@Dao
interface ContactDao {
    @Insert
    fun save(patients: PoContactEntity)
}