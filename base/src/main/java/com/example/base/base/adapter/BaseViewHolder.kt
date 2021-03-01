package com.example.base.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author 李雄厚
 *
 * @features ***
 */
class BaseViewHolder<out V : ViewDataBinding>(val binding: V) :
    RecyclerView.ViewHolder(binding.root)