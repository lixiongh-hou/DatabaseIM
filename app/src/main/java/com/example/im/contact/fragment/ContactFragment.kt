package com.example.im.contact.fragment

import androidx.lifecycle.Observer
import com.example.base.base.BaseFragment
import com.example.im.contact.entity.PoContactEntity
import com.example.im.contact.model.ContactViewModel
import com.example.im.databinding.FragmentNewsletterBinding
import com.example.im.util.LiveDataBus
import com.google.gson.Gson

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ContactFragment : BaseFragment<FragmentNewsletterBinding, ContactViewModel>() {
    override fun initView() {

        LiveDataBus.liveDataBus.with<String?>("saveContact")
            .observe(viewLifecycleOwner, Observer {
                val entity = Gson().fromJson<PoContactEntity>(it, PoContactEntity::class.java)
                val list: MutableList<PoContactEntity> = ArrayList()
                list.add(entity)
                list.addAll(mBinding.contactListView.getDataSource())
                mBinding.contactListView.setDataSource(list)
            })
    }

    override fun initData() {
        mModel.queryAllContact()

        mModel.queryAllContact.observe(viewLifecycleOwner, Observer {
            mBinding.contactListView.setDataSource(it)
        })
    }
}