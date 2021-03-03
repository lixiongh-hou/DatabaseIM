package com.example.im.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.base.base.adapter.BaseAdapter
import com.example.im.databinding.PopMenuAdapterBinding
import com.example.im.entity.PopMenuAction

/**
 * @author 李雄厚
 *
 * @features ***
 */
class PopMenuAdapter : BaseAdapter<PopMenuAction, PopMenuAdapterBinding>() {
    override fun createBinding(parent: ViewGroup, viewType: Int): PopMenuAdapterBinding =
        PopMenuAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: PopMenuAdapterBinding, data: PopMenuAction, position: Int) {
        binding.data = data
        binding.icon.setImageResource(data.iconResId)
    }
}