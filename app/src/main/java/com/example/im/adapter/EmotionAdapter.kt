package com.example.im.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.base.base.adapter.BaseAdapter
import com.example.im.databinding.ItemEmotionBinding
import com.example.im.entity.EmotionEntity

/**
 * @author 李雄厚
 *
 * @features 表情适配器
 */
class EmotionAdapter : BaseAdapter<EmotionEntity, ItemEmotionBinding>() {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemEmotionBinding =
        ItemEmotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemEmotionBinding, data: EmotionEntity, position: Int) {
        binding.face.setImageBitmap(data.icon)
    }
}