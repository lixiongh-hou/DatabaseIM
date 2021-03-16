package com.example.im.contact.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.im.R
import com.example.im.adapter.ContactAdapter
import com.example.im.contact.entity.ContactEntity
import com.example.im.contact.entity.PoContactEntity
import com.example.im.home.activity.ChatActivity
import com.example.im.home.chat.entity.QueryEntry
import com.example.im.view.CustomLinearLayoutManager
import com.google.gson.Gson

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ContactListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mRv: RecyclerView
    private var mManager: CustomLinearLayoutManager
    private var mDecoration: SuspensionDecoration
    private var mAdapter: ContactAdapter
    private var mData: MutableList<ContactEntity> = ArrayList()
    private var mList: MutableList<PoContactEntity> = ArrayList()

    /**
     * 右侧边栏导航区域
     */
    private var mIndexBar: WeChatIndexBar


    init {
        inflate(context, R.layout.contact_list, this)
        mIndexBar = findViewById(R.id.contactIndexBar)
        mRv = findViewById(R.id.contactMemberList)

        mManager = CustomLinearLayoutManager(context)
        mRv.layoutManager = mManager
        mAdapter = ContactAdapter()
        mRv.adapter = mAdapter
        //设置分割线
        mDecoration = SuspensionDecoration(context, mData)
        mRv.addItemDecoration(mDecoration)
        mIndexBar.setNeedRealIndex(false)
            .setLayoutManager(mManager)
    }

    fun getDataSource(): MutableList<PoContactEntity> {
        return mList
    }
    fun setDataSource(mList: MutableList<PoContactEntity>) {
        this.mList.clear()
        this.mList.addAll(mList)
        mData.clear()
        mData.add(
            ContactEntity(resources.getString(R.string.new_friend)).setTop(false)
                .setSuspension(false).setBaseIndexTag(
                ContactEntity.INDEX_STRING_TOP
            ) as ContactEntity
        )
        mData.add(
            ContactEntity(resources.getString(R.string.group)).setTop(false).setSuspension(false)
                .setBaseIndexTag(
                    ContactEntity.INDEX_STRING_TOP
                ) as ContactEntity
        )
        mData.add(
            ContactEntity(resources.getString(R.string.blacklist)).setTop(false)
                .setSuspension(false).setBaseIndexTag(
                ContactEntity.INDEX_STRING_TOP
            ) as ContactEntity
        )
        for (i in 0 until mList.size) {
            val bean = ContactEntity()
            bean.setId(mList[i].id)
            bean.setAvatar(mList[i].faceUrl)
            bean.setNickname(mList[i].title)
            bean.saveLocal = mList[i].saveLocal
            mData.add(bean)
        }
        mIndexBar.setSourceDates(mData).invalidate()
        mDecoration.setDatas(mData)
        mAdapter.refreshData(mData)

        Log.e("测试", Gson().toJson(mData))

        mAdapter.clickEvent = { data, _, _ ->
            val entity = QueryEntry(data.getNickname(), data.getId(), data.getAvatar(), data.saveLocal)
            Log.e("测试", Gson().toJson(entity))
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(Const.INFO, entity)
            context.startActivity(intent)
        }
    }
}