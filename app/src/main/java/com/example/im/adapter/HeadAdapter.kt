package com.example.im.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.base.base.adapter.BaseAdapter
import com.example.im.databinding.ItemHeadBinding
import com.example.im.entity.HeadEntity

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HeadAdapter : BaseAdapter<HeadEntity, ItemHeadBinding>() {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemHeadBinding =
        ItemHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemHeadBinding, data: HeadEntity, position: Int) {
        binding.url = data.url
        binding.love = data.lave
    }
}