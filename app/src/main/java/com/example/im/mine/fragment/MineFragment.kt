package com.example.im.mine.fragment

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.loadAny
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.example.base.base.BaseFragment
import com.example.base.base.BaseViewModel
import com.example.im.R
import com.example.im.databinding.FragmentMineBinding
import com.example.im.home.chat.ChatManagerKit
import com.example.im.home.chat.adapter.MessageImageHolder
import com.example.im.mine.activity.ProfileActivity
import com.example.im.util.FileUtil
import com.example.im.util.UserUtil

/**
 * @author 李雄厚
 *
 * @features ***
 */
class MineFragment : BaseFragment<FragmentMineBinding, BaseViewModel>() {

    override fun initView() {
        mBinding.fragment = this

    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        setHead(
            if (UserUtil.getMyHead().startsWith("https://") ||
                UserUtil.getMyHead().startsWith("http://")
            )
                UserUtil.getMyHead() else
                MessageImageHolder.getMediaUriFromPath(requireContext(), UserUtil.getMyHead())!!
        )
    }


    fun switchAvatar() {

        startActivity(Intent(requireContext(), ProfileActivity::class.java))

    }

    private fun setHead(head: Any) {
        mBinding.head.loadAny(head) {
            crossfade(true)
            placeholder(R.drawable.default_head)
            error(R.drawable.default_head)
            transformations(RoundedCornersTransformation(8F))
        }
    }
}