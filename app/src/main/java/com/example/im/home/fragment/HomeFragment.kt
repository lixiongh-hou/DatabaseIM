package com.example.im.home.fragment

import android.util.Log
import androidx.lifecycle.Observer
import com.example.base.base.BaseFragment
import com.example.base.base.BaseViewModel
import com.example.im.databinding.FragmentHomeBinding
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.conversation.entity.ConversationProvider
import com.example.im.util.LiveDataBus
import com.example.im.view.Menu
import com.google.gson.Gson

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {
    private lateinit var adapter: IConversationAdapter
    private lateinit var mProvider: ConversationProvider

    override fun initView() {
    }

    override fun initData() {
        adapter = ConversationListAdapter()
        mProvider = ConversationProvider()
        mBinding.conversationList.setAdapter(adapter)
        adapter.setDataProvider(mProvider)

        LiveDataBus.liveDataBus.with<String?>("setDataSource")
            .observe(viewLifecycleOwner, Observer {
                if (it == null) {
                    return@Observer
                }
                val entity = Gson().fromJson<ConversationEntity>(it, ConversationEntity::class.java)
                Log.e("测试", "数据：$entity")
                Log.e("测试", "数据：${entity.saveLocal}")
                val list: MutableList<ConversationEntity> = ArrayList()
                list.add(entity)
                mProvider.addDataSource(list)
            })
    }


}