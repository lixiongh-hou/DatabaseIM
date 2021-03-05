package com.example.im.home.fragment

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.animation.addListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.view.panel.PanelView
import com.example.base.base.BaseFragment
import com.example.base.utli.DensityUtil
import com.example.base.utli.ToastUtil.toast
import com.example.im.R
import com.example.im.databinding.FragmentChatBinding
import com.example.im.home.chat.ChatManagerKit
import com.example.im.home.chat.adapter.MessageListAdapter
import com.example.im.home.chat.util.MessageUtil
import com.example.im.home.conversation.entity.ConversationEntity
import com.example.im.home.model.MessageViewModel
import com.example.im.util.BackgroundTasks
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ChatFragment : BaseFragment<FragmentChatBinding, MessageViewModel>() {
    private var info: ConversationEntity? = null

    private var mHelper: PanelSwitchHelper? = null
    private var unfilledHeight = 0
    private var isAnimation = false

    private var mAdapter: MessageListAdapter? = null
    private var page = 1


    companion object {
        //我的的头像
        const val MY_URL = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=840492371,1920645159&fm=26&gp=0.jpg"
        fun instance(info: String) = ChatFragment().apply {
            arguments = Bundle().apply {
                putString(Const.INFO, info)
            }
        }
    }

    override fun initView() {
        mBinding.fragment = this
        arguments?.let {
            val str = it.getString(Const.INFO)
            info = Gson().fromJson(str, ConversationEntity::class.java)
        }
        setMyTitle(info?.title ?: "")
        if (mBinding.recyclerView.adapter == null) {
            mAdapter = MessageListAdapter()
            mBinding.recyclerView.adapter = mAdapter
            mBinding.recyclerView.setOnEmptySpaceClickListener {
                mHelper?.resetState()
            }
            mBinding.recyclerView.setOnLoadMoreHandler {
                BackgroundTasks.getInstance().postDelayed(Runnable {
                    page++
                    mModel.getMessage(page.toString(), info?.id ?: "", false)
                }, 500)
            }
        }

        mBinding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.toString().isNotEmpty()) {
                    if (!isAnimation) {
                        showSend()
                    }
                } else {
                    if (isAnimation) {
                        showMore()
                    }
                }
            }

        })
    }

    override fun initData() {
        mModel.getMessage(page.toString(), info!!.id, true)

        mModel.loadMessage.observe(viewLifecycleOwner, Observer {
            mAdapter?.setDataSource(it)
        })

        mModel.refreshMessage.observe(viewLifecycleOwner, Observer {
            mBinding.recyclerView.finishRefresh(it.pageNo < it.totalPage)
        })
    }

    fun btnVoice() {
        if (isAnimation) {
            showMore()
        }
        if (mBinding.recordingText.visibility == View.GONE) {
            mBinding.recordingText.visibility = View.VISIBLE
            mBinding.editText.visibility = View.GONE
            mBinding.voiceBtn.isSelected = true
            mHelper?.resetState()
        } else {
            mBinding.recordingText.visibility = View.GONE
            mBinding.editText.visibility = View.VISIBLE
            mBinding.voiceBtn.isSelected = false
            mHelper?.toKeyboardState(true)
        }

    }

    fun send(){
        val content = mBinding.editText.text.toString().trim()
        if (TextUtils.isEmpty(content)){
            "聊天消息不能为空".toast()
            return
        }
        ChatManagerKit.saveMessage(MessageUtil.sendMessage(info!!.id, content, true, info!!), false)
        mBinding.editText.setText("")
    }

    override fun onStart() {
        super.onStart()
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(this)
                .addEditTextFocusChangeListener {
                    onFocusChange { _, hasFocus ->
                        //可选实现，监听输入框焦点变化
                        if (hasFocus) {
//                            mBinding.recyclerView.scrollToEnd()
                        }
                    }
                }
                .addPanelChangeListener {
                    onKeyboard {
                        //可选实现，输入法显示回调 "唤起系统输入法"
                        emotionBtn.isSelected = false
//                        recycler_view.scrollToEnd()
                    }
                    onNone {
                        //可选实现，默认状态回调 "隐藏所有面板"
                        emotionBtn.isSelected = false
                    }
                    onPanel {
                        //可选实现，面板显示回调 "唤起面板"
                        if (it is PanelView) {

                            emotionBtn.isSelected = it.id == R.id.panel_emotion
                            recordingText.visibility = View.GONE
                            editText.visibility = View.VISIBLE
                            editText.requestFocus()
                            voiceBtn.isSelected = false
//                            mBinding.recyclerView.scrollToEnd()

                        }
                    }
                }
                .addContentScrollMeasurer { //可选，滑动模式下，可以针对内容面板内的view，定制滑动距离，默认滑动距离为 defaultDistance
                    getScrollDistance { defaultDistance ->
                        defaultDistance - unfilledHeight
                    }
                    getScrollViewId { R.id.recyclerView }
                }
                .build(false) //默认是否打开软键盘
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val childCount = recyclerView.childCount
                        if (childCount > 0) {
                            val lastChildView: View = recyclerView.getChildAt(childCount - 1)
                            val bottom: Int = lastChildView.bottom
                            val listHeight: Int =
                                recyclerView.height - recyclerView.paddingBottom
                            unfilledHeight = listHeight - bottom
                        }
                    }
                }
            })
        }
    }

    private fun showMore() {
        isAnimation = false
        mBinding.addBtn.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(
            DensityUtil.dp2px(60F),
            DensityUtil.dp2px(30F)
        )
        animator.addUpdateListener {
            val ps = mBinding.send.layoutParams
            ps.width = (it.animatedValue as Int)
            mBinding.send.layoutParams = ps
        }
        val animator1 = ValueAnimator.ofFloat(
            1F,
            0F
        )
        animator1.addUpdateListener {
            mBinding.send.alpha = (it.animatedValue as Float)
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.playTogether(animator, animator1)
        animatorSet.start()
        animatorSet.addListener(onEnd = {
            mBinding.send.visibility = View.GONE
        })
    }

    private fun showSend() {
        isAnimation = true
        mBinding.send.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(
            DensityUtil.dp2px(30F),
            DensityUtil.dp2px(60F)
        )
        animator.addUpdateListener {
            val ps = mBinding.send.layoutParams
            ps.width = (it.animatedValue as Int)
            mBinding.send.layoutParams = ps
        }
        val animator1 = ValueAnimator.ofFloat(
            0F,
            1F
        )
        animator1.addUpdateListener {
            mBinding.send.alpha = (it.animatedValue as Float)
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.playTogether(animator, animator1)
        animatorSet.start()
        animatorSet.addListener(onEnd = {
            mBinding.addBtn.visibility = View.GONE
        })
    }
}