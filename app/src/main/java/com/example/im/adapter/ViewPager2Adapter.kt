package com.example.im.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ViewPager2Adapter(
    fragmentActivity: FragmentActivity,
    private val list: MutableList<Fragment>
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]


}