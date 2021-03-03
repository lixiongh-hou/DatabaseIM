package com.example.im.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.im.R

/**
 * @author 李雄厚
 *
 * @features ***
 */
object ViewDataBinding {

    @JvmStatic
    @BindingAdapter(value = ["vpAdapter"])
    fun setVpAdapter(viewPager: ViewPager2, adapter: FragmentStateAdapter?) {
        if (adapter == null) {
            return
        }
        viewPager.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter(value = ["url", "round", "fillet"], requireAll = false)
    fun setUrl(imageView: ImageView, url: String, round: Boolean = false, fillet: Boolean = false) {
        imageView.load(url) {
            crossfade(true)
            placeholder(R.drawable.default_head)
            error(R.drawable.default_head)
            if (round) {
                transformations(CircleCropTransformation())
            }
            if (fillet) {
                transformations(RoundedCornersTransformation(8F))
            }
        }
    }

}