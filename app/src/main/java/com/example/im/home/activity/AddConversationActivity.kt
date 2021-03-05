package com.example.im.home.activity

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import com.example.base.base.BaseActivity
import com.example.base.base.adapter.BaseAdapter
import com.example.base.utli.ToastUtil.toast
import com.example.im.R
import com.example.im.adapter.HeadAdapter
import com.example.im.databinding.ActivityAddConversationBinding
import com.example.im.entity.HeadEntity
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.conversation.ConversationManagerKit
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.util.DataUtil
import com.google.gson.Gson
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
class AddConversationActivity : BaseActivity<ActivityAddConversationBinding>() {
    private lateinit var adapter: BaseAdapter<HeadEntity, *>
    val name = ObservableField<String>()
    val content = ObservableField<String>()
    val id = ObservableField<String>()
    override fun initLayout(): Int = R.layout.activity_add_conversation

    override fun initView() {
        setMyTitle(getString(R.string.add_conversation))
        adapter = HeadAdapter()
        mBinding.headRv.layoutManager = GridLayoutManager(this, 4)
        mBinding.adapter = adapter
        adapter.addAllData(DataUtil.getHeads())
        adapter.clickEvent = { _, _, position ->
            for (i in adapter.data.indices) {
                if (i == position) {
                    adapter.data[i].lave = !adapter.data[i].lave
                } else {
                    adapter.data[i].lave = false
                }
            }
            adapter.notifyDataSetChanged()

        }
        mBinding.activity = this
        mBinding.save = false
        mBinding.edtId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                for (entity in ConversationManagerKit.mProvider!!.getDataSource()) {
                    if (entity.id == s.toString()) {
                        mBinding.textId.visibility = View.VISIBLE
                    } else {
                        mBinding.textId.visibility = View.GONE
                    }
                }
            }
        })
    }


    fun isSave() {
        mBinding.save = !mBinding.save!!
    }

    fun confirm() {
        if (TextUtils.isEmpty(id.get())) {
            "请输入对方ID".toast()
            return
        }
        if (mBinding.textId.visibility == View.VISIBLE) {
            "数据库中已有相同ID".toast()
            return
        }
        if (TextUtils.isEmpty(name.get())) {
            "请输入名称".toast()
            return
        }
        if (TextUtils.isEmpty(content.get())) {
            "请输入内容".toast()
            return
        }
        if (!isSelect()) {
            "请选择一个对方头像".toast()
            return
        }
        val intent = Intent()
        val entity = ConversationEntity(
            1, UUID.randomUUID().toString(),
            id.get()!!, getHead(), name.get()!!,
            false, false, System.currentTimeMillis(),
            PoMessageEntity(
                id.get()!!, UUID.randomUUID().toString(), PoMessageEntity.MSG_TYPE_TEXT,
                PoMessageEntity.MSG_STATUS_SEND_SUCCESS, false, true, "", content.get()!!,
                getHead(), 0, 0, System.currentTimeMillis(), true, true
            )
        )
        entity.saveLocal = mBinding.save!!
        val info = Gson().toJson(entity)
        intent.putExtra(Const.INFO, info)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun isSelect(): Boolean {
        var cont = 0
        for (i in adapter.data.indices) {
            if (adapter.data[i].lave) {
                cont++
            }
        }
        return cont >= 1
    }

    private fun getHead(): String {
        for (i in adapter.data.indices) {
            if (adapter.data[i].lave) {
                return adapter.data[i].url
            }
        }
        return "Https://"
    }
}