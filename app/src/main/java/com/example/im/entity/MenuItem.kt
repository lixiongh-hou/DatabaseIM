package com.example.im.entity

import android.view.View

/**
 * @author 李雄厚
 *
 * @features ***
 */
data class MenuItem (
        val item: String,
        val itemResId: Int = View.NO_ID
)