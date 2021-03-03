package com.example.im.home.fragment

import com.example.base.base.BaseFragment
import com.example.base.base.BaseViewModel
import com.example.im.databinding.FragmentHomeBinding
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.adapter.ConversationListAdapter
import com.example.im.home.conversation.adapter.IConversationAdapter
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.conversation.entity.ConversationProvider
import com.example.im.view.Menu

/**
 * @author 李雄厚
 *
 * @features ***
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>() {
    private lateinit var adapter: IConversationAdapter
    private lateinit var mProvider: ConversationProvider

    companion object{
        const val MY_URL = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=840492371,1920645159&fm=26&gp=0.jpg"
    }
    override fun initView() {
    }

    override fun initData() {
        adapter = ConversationListAdapter()
        mProvider = ConversationProvider()
        mBinding.conversationList.setAdapter(adapter)
        mProvider.setDataSource(getData())
        adapter.setDataProvider(mProvider)
        mBinding.conversationList.setOnItemClickListener { view, position, messageInfo ->
            mProvider.setDataSource(getData1())
        }
    }

    private fun getData(): MutableList<ConversationEntity>{
        val data : MutableList<ConversationEntity> = ArrayList()
        data.add(ConversationEntity(1, "111", "1", MY_URL, "测试", false, false, System.currentTimeMillis(), PoMessageEntity("1", "你好")))
        return data
    }
    private fun getData1(): MutableList<ConversationEntity>{
        val data : MutableList<ConversationEntity> = ArrayList()
        data.add(ConversationEntity(2, "111", "1", MY_URL, "测试测试", false, false, System.currentTimeMillis(), PoMessageEntity("1", "你好")))
        return data
    }

}