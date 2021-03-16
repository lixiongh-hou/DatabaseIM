package com.example.im.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.base.base.BaseApp
import com.example.base.base.adapter.BaseAdapter
import com.example.im.R
import com.example.im.contact.entity.ContactEntity
import com.example.im.contact.entity.PoContactEntity
import com.example.im.databinding.ItemContactBinding

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ContactAdapter : BaseAdapter<ContactEntity, ItemContactBinding>() {
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemContactBinding =
        ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemContactBinding, data: ContactEntity, position: Int) {
        binding.data = data
        if (position < this.data.size - 1) {
            val tag1 = data.getSuspensionTag()
            val tag2 = this.data[position + 1].getBaseIndexTag()
            // tag不同时对item的分割线进行重新处理
            if (TextUtils.equals(tag1, tag2)) {
                binding.line.visibility = View.VISIBLE
            } else {
                binding.line.visibility = View.INVISIBLE
            }
        } else {
            binding.line.visibility = View.INVISIBLE
        }

        when {
            TextUtils.equals(
                BaseApp.instance.getString(R.string.new_friend),
                data.getNickname()
            ) -> binding.avatar.setImageResource(R.drawable.group_new_friend)
            TextUtils.equals(
                BaseApp.instance.getString(R.string.group),
                data.getNickname()
            ) -> binding.avatar.setImageResource(R.drawable.group_common_list)
            TextUtils.equals(
                BaseApp.instance.getString(R.string.blacklist),
                data.getNickname()
            ) -> binding.avatar.setImageResource(R.drawable.group_black_list)
            else -> {
                binding.avatar.load(data.getAvatar()) {
                    crossfade(true)
                    placeholder(R.drawable.default_head)
                    error(R.drawable.default_head)
                    transformations(RoundedCornersTransformation(8F))
                }
            }
        }
    }
}