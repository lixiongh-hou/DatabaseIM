package com.example.im.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.base.base.adapter.BaseAdapter
import com.example.im.R
import com.example.im.databinding.PopMenuAdapterBinding
import com.example.im.entity.PopMenuAction

/**
 * @author 李雄厚
 *
 * @features ***
 */
class PopMenuAdapter : BaseAdapter<PopMenuAction, PopMenuAdapterBinding>() {
    var itemOnClick: ((View?, Int, PopMenuAction) -> Unit)? = null
    override fun createBinding(parent: ViewGroup, viewType: Int): PopMenuAdapterBinding =
        PopMenuAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: PopMenuAdapterBinding, data: PopMenuAction, position: Int) {
        binding.data = data
        binding.icon.setImageResource(data.iconResId)
        when (position) {
            0 -> {
                binding.itemView.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.pop_top_btn_bj)
            }
            this.data.size - 1 -> {
                binding.itemView.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.pop_bottom_btn_bj)
            }
            else -> {
                binding.itemView.background =
                    ContextCompat.getDrawable(binding.root.context, R.drawable.pop_btn_bj)
            }
        }
        binding.itemView.setOnClickListener {
            itemOnClick?.invoke(it, position, data)
        }
    }
}