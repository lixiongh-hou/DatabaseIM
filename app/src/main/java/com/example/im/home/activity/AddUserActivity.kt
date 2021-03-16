package com.example.im.home.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.GridLayoutManager
import com.example.base.base.BaseActivity
import com.example.base.base.adapter.BaseAdapter
import com.example.base.utli.ToastUtil.toast
import com.example.im.R
import com.example.im.adapter.HeadAdapter
import com.example.im.contact.entity.PoContactEntity
import com.example.im.databinding.ActivityAddUserBinding
import com.example.im.entity.HeadEntity
import com.example.im.home.chat.util.ContactKit
import com.example.im.util.DataUtil
import com.google.gson.Gson
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
class AddUserActivity : BaseActivity<ActivityAddUserBinding>() {
    private lateinit var adapter: BaseAdapter<HeadEntity, *>
    var id = ObservableField<String>()
    var name = ObservableField<String>()
    override fun initLayout(): Int = R.layout.activity_add_user

    override fun initView() {
        setMyTitle(getString(R.string.add_user))
        mBinding.activity = this
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
        mBinding.save = false
    }


    fun isSave(){
        mBinding.save = !mBinding.save!!
    }

    fun random(){
        id.set(UUID.randomUUID().toString())
    }

    fun confirm(){
        if (TextUtils.isEmpty(id.get())){
            "用户ID不能为空".toast()
            return
        }
        if (TextUtils.isEmpty(name.get())){
            "用户名字不能为空".toast()
            return
        }
        if (!isSelect()){
            "请选择一个头像".toast()
            return
        }
        val entity = PoContactEntity(id.get()!!, name.get()!!, getHead())
        entity.saveLocal = mBinding.save!!
        ContactKit.saveContact(entity) {
            "保存成功".toast()
            val info = Gson().toJson(it)
            val intent = Intent()
            intent.putExtra(Const.INFO, info)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

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