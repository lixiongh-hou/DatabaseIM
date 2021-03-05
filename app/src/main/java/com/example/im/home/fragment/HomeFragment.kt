package com.example.im.home.fragment

import android.content.Intent
import android.graphics.Point
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.Observer
import com.example.base.base.BaseFragment
import com.example.im.MainActivity
import com.example.im.databinding.FragmentHomeBinding
import com.example.im.home.activity.ChatActivity
import com.example.im.home.conversation.ConversationManagerKit
import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.model.HomeViewModel
import com.example.im.util.LiveDataBus
import com.example.im.view.AnywherePopupWindow
import com.google.gson.Gson

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private lateinit var adapter: IConversationAdapter
    private val point: Point = Point()

    override fun initView() {
        adapter = ConversationListAdapter()
        mBinding.conversationList.setAdapter(adapter)

        LiveDataBus.liveDataBus.with<String?>("saveConversation")
            .observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    return@Observer
                }
                val entity = Gson().fromJson<ConversationEntity>(it, ConversationEntity::class.java)
                Log.e("测试", "数据：$entity")
                Log.e("测试", "数据：${entity.saveLocal}")
                ConversationManagerKit.saveConversation(entity, entity.saveLocal)
            })
        mBinding.conversationList.setOnItemLongClickListener { _, position, messageInfo ->
            AnywherePopupWindow(requireActivity()).apply {
                items(
                    120, if (messageInfo.top) "取消置顶" else "置顶聊天",
                    if (messageInfo.unRead == 0) "设置未读" else "取消未读",
                    "删除消息"
                )
                show(point)
                setOnItemClickListener { _, pos ->
                    when (pos) {
                        0 -> ConversationManagerKit.setConversationTop(position, messageInfo)

                    }
                }
            }
        }
        mBinding.conversationList.setOnItemClickListener { _, _, messageInfo ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra(Const.INFO, Gson().toJson(messageInfo))
            startActivity(intent)
        }
        (this.requireActivity() as MainActivity).registerMyTouchListener {
            onTouch { ev ->
                if (ev?.action == MotionEvent.ACTION_DOWN) {
                    point.x = ev.rawX.toInt()
                    point.y = ev.rawY.toInt()
                }
            }
        }
    }

    override fun initData() {
        mModel.loadConversation()
        mModel.success.observe(viewLifecycleOwner, Observer {
            adapter.setDataProvider(it)
        })
    }
}