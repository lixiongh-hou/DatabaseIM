package com.example.im.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
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

    @Query("SELECT * FROM contact ")
    fun queryAll(): MutableList<PoContactEntity>
}