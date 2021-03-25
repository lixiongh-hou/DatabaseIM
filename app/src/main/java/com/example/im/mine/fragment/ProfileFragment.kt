package com.example.im.mine.fragment

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.loadAny
import coil.transform.RoundedCornersTransformation
import com.example.base.base.BaseFragment
import com.example.base.base.BaseViewModel
import com.example.im.R
import com.example.im.databinding.ActivityHeadBinding
import com.example.im.databinding.FragmentProfileBinding
import com.example.im.home.chat.ChatManagerKit
import com.example.im.home.chat.adapter.MessageImageHolder
import com.example.im.mine.activity.HeadActivity
import com.example.im.util.FileUtil
import com.example.im.util.UserUtil

const val SWITCH_AVATAR = 0x1013

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding, BaseViewModel>() {

    override fun onBackClickListener() {
        requireActivity().finish()
    }

    override fun initView() {
        setMyTitle("个人信息")
        mBinding.fragment = this

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


    private fun setHead(head: Any) {
        mBinding.head.loadAny(head) {
            crossfade(true)
            placeholder(R.drawable.default_head)
            error(R.drawable.default_head)
            transformations(RoundedCornersTransformation(8F))
        }
    }

    override fun initData() {
    }

    fun lookHead() {
        startActivity(Intent(requireContext(), HeadActivity::class.java))
    }
    fun changeHead(){
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, SWITCH_AVATAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == SWITCH_AVATAR) {
                val videoPath: String = FileUtil.getPathFromUri(data?.data)
                Log.e("测试", videoPath)
                setHead(data?.data!!)
                UserUtil.setMyHead(videoPath)
                ChatManagerKit.updateSelfHead(videoPath)
            }
        }
    }
}